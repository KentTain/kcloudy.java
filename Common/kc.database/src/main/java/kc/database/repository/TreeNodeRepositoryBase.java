package kc.database.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.persistence.criteria.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import kc.framework.base.EntityBase;
import kc.framework.base.TreeNode;
import kc.framework.extension.StringExtensions;


//@Repository
@Transactional(readOnly = true)
public class TreeNodeRepositoryBase<T extends TreeNode<T>, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements ITreeNodeRepository<T, ID> {
	private Logger logger = LoggerFactory.getLogger(TreeNodeRepositoryBase.class);

	private final EntityManager em;

	private final String TreeCodeSplitIdWithChar = "-";
	public TreeNodeRepositoryBase(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);

		// Keep the EntityManager around to used from the newly introduced methods.
		this.em = entityManager;
	}

	public TreeNodeRepositoryBase(Class<T> domainClass, EntityManager entityManager) {
		this(JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager), entityManager);
	}

	@Override
	public boolean support(String modelType) {
		return super.getDomainClass().getName().equals(modelType);
	}

	@Override
	public String getTreeCodeSplitIdWithChar()
	{
		return TreeCodeSplitIdWithChar;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean executeUpdateSql(String sql, Object... parameters) {
		try {
			logger.debug("---execute sql: " + sql);

			Query query = em.createNativeQuery(sql);
			int result = query.executeUpdate();
			// entityManager.close();

			return result > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K extends EntityBase> List<K> sqlQuery(String sql) {
		try {
			logger.debug("---execute sql: " + sql);

			Query query = em.createQuery(sql);
			List<K> result = query.getResultList();
			// entityManager.close();

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *	根据树结构的Id，获取节点及其子节点下的所有数据（包括子节点下的子节点）<br/>
	 *		使用Lazyloading的四种方法: <br/>
	 *		https://thoughts-on-java.org/5-ways-to-initialize-lazy-relations-and-when-to-use-them/
	 *@param clazz 泛型T的Class类型
	 *@param id 树结构的Id
	 *@return T 返回结果，形如：<br/>
	 * --node  --> Id == id	<br/>
	 *		--node			<br/>
	 *			--node		<br/>
	 *			--node		<br/>
	 *				--node	<br/>
	 *		--node			<br/>
	 *			--node		<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public T getTreeNodeWithNestChildById(Class<T> clazz, int id) {
		//方法一：ITreeNodeRepository类使用@EntityGraph注解指向TreeNode类的注解@NamedEntityGraph
//		return em.find(clazz, id);

		// 方法二：使用Dynamic Entity Graph，通过方法addSubgraph("childNodes")获取子类的引用
//		EntityGraph<T> graph = this.em.createEntityGraph(clazz);
//		Subgraph<?> employeesGraph = graph.addSubgraph("childNodes");
//		java.util.Map<String, Object> props = new java.util.HashMap<>();
//		props.put("javax.persistence.loadgraph", graph);
//		return em.find(clazz, id, props);

		// 方法三：使用Fetch Join in Criteria API
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		root.fetch("childNodes", JoinType.INNER);
		query.select(root);
		query.where(builder.equal(root.get("id"), id));
		return (T)em.createQuery(query).getSingleResult();

		// 方法四：Fetch Join in JPQL
//		 Query query = em.createQuery("SELECT d FROM MenuNode d JOIN FETCH
//		 d.ChildNodes e WHERE d.Id = :id");
//		 query.setParameter("id", id);
//		 return (T) query.getSingleResult();
	}

	/**
	 *	根据树结构的Name，获取包含其父节点（包含父节点上的父节点）及其子节点（子节点的子节点）<br/>
	 *		使用Lazyloading的四种方法: <br/>
	 *		https://thoughts-on-java.org/5-ways-to-initialize-lazy-relations-and-when-to-use-them/
	 *@param clazz 泛型T的Class类型
	 *@param name 树结构的Name
	 *@return T 返回结果，形如：<br/>
	 * --node<br/>
	 *		--node --> Name == name （TreeCode：1-1001）<br/>
	 *			--node		<br/>
	 *			--node		<br/>
	 *				--node	<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public List<T> findAllTreeNodesWithNestParentAndChildByName(Class<T> clazz, String name){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		// Predicate 过滤条件 构建where字句可能的各种条件
		// 这里用List存放多种查询条件,实现动态查询
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.isFalse(root.get("isDeleted")));
		if (!StringExtensions.isNullOrEmpty(name)) {
			predicates.add(builder.and(builder.like(root.get("name"), "%" + name + "%")));
		}

		query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
		TypedQuery<T> typedQuery = em.createQuery(query); // TypedQuery执行查询与获取元模型实例
		List<T> treeNodes = typedQuery.getResultList();

		if (treeNodes == null || treeNodes.size() == 0){
			return new ArrayList<T>();
		}

		Iterator<String> codeIterator = treeNodes.stream().map(m -> m.getTreeCode()).iterator();
		Set<Integer> idSet = new HashSet<Integer>();
		while(codeIterator.hasNext())
		{
			String code = codeIterator.next();
			Integer[] ids = StringExtensions.arrayFromCommaDelimitedIntegersBysplitChar(code, TreeCodeSplitIdWithChar);
			idSet.addAll(Arrays.asList(ids));
		}

		if(idSet == null || idSet.size() == 0)
			return treeNodes;

		builder = em.getCriteriaBuilder();
		query = builder.createQuery(clazz);
		root = query.from(clazz);
		//root.fetch("childNodes", JoinType.LEFT);

		// Predicate 过滤条件 构建where字句可能的各种条件
		// 这里用List存放多种查询条件,实现动态查询
		predicates = new ArrayList<>();
		predicates.add(builder.isFalse(root.get("isDeleted")));
		if (idSet.size() > 0) {
			Expression<Integer> idExpression = root.get("id");
			List<Integer> array = idSet.stream().collect(Collectors.toList());
			predicates.add(builder.and(idExpression.in(array)));
		}

		// 设置排序规则
		Order order = builder.asc(root.get("index"));
		query.orderBy(order);
		query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
		typedQuery = em.createQuery(query); // TypedQuery执行查询与获取元模型实例

		List<T> treeList = typedQuery.getResultList();
		Iterator<T> treeIterator = treeList.stream().filter(n -> n.getParentNode() == null).iterator();

		List<T> result = new ArrayList<T>();
		while(treeIterator.hasNext())
		{
			T rootNode = treeIterator.next();
			NestTreeNodeWithChild(rootNode, treeList);
			result.add(rootNode);
		}
		return result;
	}

    /**
     *	根据树结构的Name，获取包含其父节点（包含父节点上的父节点）及其子节点（子节点的子节点）<br/>
     *		使用Lazyloading的四种方法: <br/>
     *		https://thoughts-on-java.org/5-ways-to-initialize-lazy-relations-and-when-to-use-them/
     *@param clazz 泛型T的Class类型
     *@param ids 树结构的查询条件
     *@return T 返回结果，形如：<br/>
     * --node<br/>
     *		--node --> Name == name （TreeCode：1-1001）<br/>
     *			--node		<br/>
     *			--node		<br/>
     *				--node	<br/>
     */
    @Override
    @Transactional(readOnly = false)
    public List<T> findAllTreeNodesWithNestParentAndChildByIds(Class<T> clazz, List<Integer> ids){
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        builder = em.getCriteriaBuilder();
        query = builder.createQuery(clazz);
        root = query.from(clazz);
        //root.fetch("childNodes", JoinType.LEFT);

        // Predicate 过滤条件 构建where字句可能的各种条件
        // 这里用List存放多种查询条件,实现动态查询
		List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.isFalse(root.get("isDeleted")));
        if (ids.size() > 0) {
            Expression<Integer> idExpression = root.get("id");
            List<Integer> array = ids.stream().collect(Collectors.toList());
            predicates.add(builder.and(idExpression.in(array)));
        }

        // 设置排序规则
        Order order = builder.asc(root.get("index"));
        query.orderBy(order);
        query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
		TypedQuery<T> typedQuery = em.createQuery(query); // TypedQuery执行查询与获取元模型实例

        List<T> treeList = typedQuery.getResultList();
		if (treeList == null || treeList.size() == 0){
			return new ArrayList<T>();
		}

		Iterator<T> treeIterator = treeList.stream().filter(n -> n.getParentNode() == null).iterator();

        List<T> result = new ArrayList<T>();
        while(treeIterator.hasNext())
        {
            T rootNode = treeIterator.next();
            NestTreeNodeWithChild(rootNode, treeList);
            result.add(rootNode);
        }
        return result;
    }
	
	/**
	 * 根据树结构的TreeCode，获取包含其父节点（包含父节点上的父节点）及其子节点（子节点的子节点）<br/>
	 *		使用Lazyloading的四种方法: <br/>
	 *		https://thoughts-on-java.org/5-ways-to-initialize-lazy-relations-and-when-to-use-them/
	 *@param clazz 泛型T的Class类型
	 *@param treeCode 树结构的Name
	 *@return T 返回结果，形如：<br/>
	 * --node  					<br/>
	 *     --node				<br/>
	 *         --node --> Id == id （TreeCode：1-1001-10001）<br/>
	 *             --node		<br/>
	 *             --node		<br/>
	 */
	@Override
	@Transactional(readOnly = false)
	public List<T> findTreeNodesWithNestParentAndChildByTreeCode(Class<T> clazz, String treeCode){
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(clazz);
		Root<T> root = query.from(clazz);
		//root.fetch("childNodes", JoinType.INNER);

		// Predicate 过滤条件 构建where字句可能的各种条件
		// 这里用List存放多种查询条件,实现动态查询
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.isFalse(root.get("isDeleted")));
		Integer[] ids = StringExtensions.arrayFromCommaDelimitedIntegersBysplitChar(treeCode, TreeCodeSplitIdWithChar);
		if (ids.length > 0) {
			Expression<Integer> idExpression = root.get("id");
			predicates.add(builder.and(idExpression.in(Arrays.asList(ids))));
		}

		// 设置排序规则
		Order order = builder.asc(root.get("index"));
		query.orderBy(order);
		query.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
		TypedQuery<T> typedQuery = em.createQuery(query); // TypedQuery执行查询与获取元模型实例

		List<T> treeList = typedQuery.getResultList();
		if (treeList == null || treeList.size() == 0){
			return new ArrayList<T>();
		}

		Iterator<T> treeIterator = treeList.stream().filter(n -> n.getParentNode() == null).iterator();

		List<T> result = new ArrayList<T>();
		while(treeIterator.hasNext())
		{
			T rootNode = treeIterator.next();
			NestTreeNodeWithChild(rootNode, treeList);
			result.add(rootNode);
		}
		return result;
	}

	private void NestTreeNodeWithChild(T parent, List<T> entities) {
		if (parent == null || entities == null) return;
		if (entities.size() <= 0) return;
        List<T> child = entities.stream()
				.filter(m -> m.getParentNode() != null && m.getParentNode().getId() == parent.getId())
				.collect(Collectors.toList());
        parent.setChildNodes(child);
        for (T children : child)
        {
            children.setParentNode(parent);
            NestTreeNodeWithChild(children, entities);
        }
    }

	/**
	 * 根据树节点Id，删除树节点及其下面的子节点
	 *@param clazz 泛型T的Class类型
	 *@param id 树结构的id
	 *@return boolean
	 */
	@Override
	@Transactional
	public boolean removeTreeNodeWithChildById(Class<T> clazz, int id) {
		T node = em.find(clazz, id);
		return removeTreeNodeWithChild(node, true);
	}
	/**
	 * 删除树节点列表及其下面的子节点
	 *@param nodes 树节点列表
	 *@return int 影响行数
	 */
	@Override
	@Transactional
	public int removeTreeNodesWithChild(Iterable<T> nodes) {
		int result = 0; 
		
		Iterator<T> nodeIterator = nodes.iterator();
		while(nodeIterator.hasNext())
		{
			T node = nodeIterator.next();
			boolean success = removeTreeNodeWithChild(node, false);
			if(success) result++;
		}
		
		em.flush();
		
		return result;
	}
	/**
	 * 删除树节点及其下面的子节点
	 *@return boolean
	 */
	@Override
	@Transactional
	public boolean removeTreeNodeWithChild(T node) {
		return removeTreeNodeWithChild(node, true);
	}
	
	private boolean removeTreeNodeWithChild(T node, boolean isSave) {
		try {
			//删除子节点
			removeNestedChild(node);
			
			em.remove(em.contains(node) ? node : em.merge(node));
			
			if(isSave)
				em.flush();
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private void removeNestedChild(T node) {
		//删除子节点
		for(T childNode : node.getChildNodes())
		{
			childNode.setParentNode(null);
			node.getChildNodes().remove(childNode);
			removeNestedChild(childNode);
			em.remove(em.contains(childNode) ? childNode : em.merge(childNode));
		}
		
		em.flush();
	}
	
	/**
	 * 保持树结构类别，同时更新树的扩展属性：TreeCode、Level、Leaf
	 *@param entities 需要保持树结构列表
	 *@return List<T> 返回带Id的树结构列表
	 */
	@Override
	@Transactional
	public List<T> saveAllTreeNodeWithExtensionFields(Iterable<T> entities) {
		List<T> resultList = super.saveAll(entities);
		//em.flush();
		List<T> rootList = resultList.stream().filter(m -> m.getParentNode() == null).collect(Collectors.toList());
		for (T parentNode : rootList) {
			parentNode.setLevel(1);
			parentNode.setTreeCode(Integer.toString(parentNode.getId()));

			SetTreeCodeWithNestChild(parentNode);
		}

		super.flush();

		return resultList;
	}
	/**
	 *   更新字段的TreeCode、Level、Leaf等字段值，TreeCode值格式（例如：1-1001-10005）
	 * @param parent
	 */
	public void SetTreeCodeWithNestChild(T parent) {
		List<T> child = parent.getChildNodes();
		parent.setLeaf(child.size() <= 0);
		for (T children : child) {
			children.setLevel(parent.getLevel() + 1);
			children.setTreeCode(parent.getTreeCode() + TreeCodeSplitIdWithChar + children.getId());
			SetTreeCodeWithNestChild(children);
		}
	}
}