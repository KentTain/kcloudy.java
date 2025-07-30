package kc.service.util;

import kc.dto.TreeNodeDTO;
import kc.dto.TreeNodeSimpleDTO;
import kc.framework.base.TreeNode;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TreeNodeUtil {
    /**
     * 树形选中
     *
     * @param scoreList 所有节点
     * @param checkList 已选中节点的Id列表
     * @param <T>       树形对象
     * @return
     */
    public static <T extends TreeNodeSimpleDTO<T>> List<T> GetTreeNodeSimpleWithChildren(List<T> scoreList, List<Integer> checkList) {
        for (T dto : scoreList) {
            if (dto.getChildren().size() > 0) {
                if (checkList.contains(dto.getId()))
                    dto.setChecked(true);
                GetTreeNodeSimpleWithChildren(dto.getChildren(), checkList);
            } else {
                if (checkList.contains(dto.getId())) {
                    dto.setChecked(true);
                }
            }
        }
        return scoreList;
    }

    /**
     * 将List<TreeNode>对象转化为树形结构的List<TreeNode>（rootTreeNodes）使用范例：<br/>
     * List<T> rootTreeNodes = allTreeNodes.stream().filter(m -> m.getParentId()== null).collect(Collectors.toList()); <br/>
     * foreach(T level1 in rootTreeNodes){
     * TreeNodeUtil.NestTreeNode(level1, allTreeNodes);
     * }
     *
     * @param parent       根节点：ParentId==null
     * @param allTreeNodes 对象列表对象（非树结构）
     * @param predict      筛选节点条件
     * @param <T>          对象
     */
    public static <T extends TreeNode<T>> void NestTreeNode(T parent, List<T> allTreeNodes, Predicate<T> predict) {
        if (null == predict) {
            List<T> child = allTreeNodes.stream()
                    .filter(m -> null != m.getParentNode() && m.getParentNode().getId() == parent.getId())
                    .collect(Collectors.toList());
            parent.setChildNodes(child);
            List<T> sortedChild = child.stream()
                    .sorted(Comparator.comparingInt(m -> m.getIndex()))
                    .collect(Collectors.toList());
            for (T children : sortedChild) {
                NestTreeNode(children, allTreeNodes, null);
            }
        } else {
            List<T> child = allTreeNodes.stream()
                    .filter(m -> null != m.getParentNode() && m.getParentNode().getId() == parent.getId())
                    .filter(predict)
                    .collect(Collectors.toList());
            parent.setChildNodes(child);
            List<T> sortedChild = child.stream()
                    .sorted(Comparator.comparingInt(TreeNode::getIndex))
                    .collect(Collectors.toList());
            for (T children : sortedChild) {
                NestTreeNode(children, allTreeNodes, predict);
            }
        }
    }

    /**
     * 将List<TreeNodeDTO>对象转化为树形结构的List<TreeNodeDTO>（rootTreeNodes）使用范例：<br/>
     * List<T> rootTreeNodes = allTreeNodes.stream().filter(m -> m.getParentId()== null).collect(Collectors.toList()); <br/>
     * foreach(T level1 in rootTreeNodes){
     * TreeNodeUtil.NestTreeNode(level1, allTreeNodes);
     * }
     *
     * @param parent       根节点：ParentId==null
     * @param allTreeNodes 对象列表对象（非树结构）
     * @param predict      筛选节点条件
     * @param <T>          对象
     */
    public static <T extends TreeNodeDTO<T>> void NestTreeNodeDTO(T parent, List<T> allTreeNodes, Predicate<T> predict) {
        if (null == predict) {
            List<T> child = allTreeNodes.stream()
                    .filter(m -> null != m.getParentNode() && m.getParentNode().getId() == parent.getId())
                    .collect(Collectors.toList());
            parent.setChildren(child);
            List<T> sortedChild = child.stream()
                    .sorted(Comparator.comparingInt(m -> m.getIndex()))
                    .collect(Collectors.toList());
            for (T children : sortedChild) {
                NestTreeNodeDTO(children, allTreeNodes, null);
            }
        } else {
            List<T> child = allTreeNodes.stream()
                    .filter(m -> null != m.getParentNode() && m.getParentNode().getId() == parent.getId())
                    .filter(predict)
                    .collect(Collectors.toList());
            parent.setChildren(child);
            List<T> sortedChild = child.stream()
                    .sorted(Comparator.comparingInt(TreeNodeDTO::getIndex))
                    .collect(Collectors.toList());
            for (T children : sortedChild) {
                NestTreeNodeDTO(children, allTreeNodes, predict);
            }
        }
    }

    /**
     * 将List<TreeNodeSimpleDTO>对象转化为树形结构的List<TreeNodeSimpleDTO>（rootTreeNodes）使用范例：<br/>
     * List<T> rootTreeNodes = allTreeNodes.stream().filter(m -> m.getParentId()== null).collect(Collectors.toList()); <br/>
     * foreach(T level1 in rootTreeNodes){
     * TreeNodeUtil.NestTreeNode(level1, allTreeNodes);
     * }
     *
     * @param parent       根节点：ParentId==null
     * @param allTreeNodes 对象列表对象（非树结构）
     * @param predict      筛选节点条件
     * @param <T>          对象
     */
    public static <T extends TreeNodeSimpleDTO<T>> void NestSimpleTreeNodeDTO(T parent, List<T> allTreeNodes, Predicate<T> predict) {
        if (null == predict) {
            List<T> child = allTreeNodes.stream()
                    .filter(m -> null != m.getParentNode() && m.getParentNode().getId() == parent.getId())
                    .collect(Collectors.toList());
            parent.setChildren(child);
            List<T> sortedChild = child.stream()
                    .sorted(Comparator.comparingInt(m -> m.getIndex()))
                    .collect(Collectors.toList());
            for (T children : sortedChild) {
                NestSimpleTreeNodeDTO(children, allTreeNodes, null);
            }
        } else {
            List<T> child = allTreeNodes.stream()
                    .filter(m -> null != m.getParentNode() && m.getParentNode().getId() == parent.getId())
                    .filter(predict)
                    .collect(Collectors.toList());
            parent.setChildren(child);
            List<T> sortedChild = child.stream()
                    .sorted(Comparator.comparingInt(TreeNodeSimpleDTO::getIndex))
                    .collect(Collectors.toList());
            for (T children : sortedChild) {
                NestSimpleTreeNodeDTO(children, allTreeNodes, predict);
            }
        }
    }


    /**
     * 排除需要删除的树节点后，只保留{maxLevel}级树节点；
     *
     * @param tree  需要筛选的树结构数据
     * @param maxLevel 最大的节点深度
     * @param excludeIds 需要排除的树Id列表
     * @param <T>
     * @return
     */
    public static <T extends TreeNode<T>> T GetNeedLevelTreeNode(T tree, int maxLevel, List<Integer> excludeIds) {

        if (excludeIds != null
                && tree.getChildNodes() != null
                && tree.getChildNodes().stream().anyMatch(o -> excludeIds.contains(o.getId()))) {
            List<T> removeList = tree.getChildNodes().stream()
                    .filter(o -> excludeIds.contains(o.getId()))
                    .collect(Collectors.toList());
            tree.getChildNodes().removeAll(removeList);
        }
        if (tree.getLevel() >= maxLevel)
            tree.setChildNodes(null);

        if (null != tree.getChildNodes()) {
            for (T children : tree.getChildNodes()) {
                GetNeedLevelTreeNode(children, maxLevel, excludeIds);
            }
        }

        return tree;
    }

    /**
     * 排除需要删除的树节点后，只保留{maxLevel}级树节点；
     *
     * @param tree  需要筛选的树结构数据
     * @param maxLevel 最大的节点深度
     * @param excludeIds 需要排除的树Id列表
     * @param <T>
     * @return
     */
    public static <T extends TreeNodeDTO<T>> T GetNeedLevelTreeNodeDTO(T tree, int maxLevel, List<Integer> excludeIds, List<Integer> checkList) {

        if (excludeIds != null
                && tree.getChildren() != null
                && tree.getChildren().stream().anyMatch(o -> excludeIds.contains(o.getId()))) {
            List<T> removeList = tree.getChildren().stream()
                    .filter(o -> excludeIds.contains(o.getId()))
                    .collect(Collectors.toList());
            tree.getChildren().removeAll(removeList);
        }
        if (tree.getLevel() >= maxLevel)
            tree.setChildren(null);
        if (null != checkList && checkList.contains(tree.getId()))
            tree.setChecked(true);
        if (null != tree.getChildren()) {
            for (T children : tree.getChildren()) {
                GetNeedLevelTreeNodeDTO(children, maxLevel, excludeIds, checkList);
            }
        }

        return tree;
    }

    /**
     * 排除需要删除的树节点后，只保留{maxLevel}级树节点；
     *
     * @param tree  需要筛选的树结构数据
     * @param maxLevel 最大的节点深度
     * @param excludeIds 需要排除的树Id列表
     * @param <T>
     * @return
     */
    public static <T extends TreeNodeSimpleDTO<T>> T GetNeedLevelSimpleTreeNodeDTO(T tree, int maxLevel, List<Integer> excludeIds, List<Integer> checkList) {
        if (excludeIds != null
                && tree.getChildren() != null
                && tree.getChildren().stream().anyMatch(o -> excludeIds.contains(o.getId()))) {
            List<T> removeList = tree.getChildren().stream()
                    .filter(o -> excludeIds.contains(o.getId()))
                    .collect(Collectors.toList());
            tree.getChildren().removeAll(removeList);
        }

        if (tree.getLevel() >= maxLevel)
            tree.setChildren(null);

        if (null != checkList && checkList.contains(tree.getId()))
            tree.setChecked(true);

        if (null != tree.getChildren()) {
            for (T children : tree.getChildren()) {
                GetNeedLevelSimpleTreeNodeDTO(children, maxLevel, excludeIds, checkList);
            }
        }

        return tree;
    }


    /**
     * 排除需要删除的树节点后，只保留{maxLevel}级树节点；
     *
     * @param treeList  需要筛选的树结构数据列表
     * @param maxLevel 最大的节点深度
     * @param excludeIds 需要排除的树Id列表
     * @return
     */
    public static <T extends TreeNode<T>> List<T> LoadNeedLevelTreeNode(List<T> treeList, int maxLevel, List<Integer> excludeIds) {
        for (T child : treeList) {
            GetNeedLevelTreeNode(child, maxLevel, excludeIds);
        }

        return treeList;
    }

    /**
     * 排除需要删除的树节点后，只保留{maxLevel}级树节点；
     *
     * @param treeList  需要筛选的树结构数据列表
     * @param maxLevel 最大的节点深度
     * @param excludeIds 需要排除的树Id列表
     * @return
     */
    public static <T extends TreeNodeDTO<T>> List<T> LoadNeedLevelTreeNodeDTO(List<T> treeList, int maxLevel, List<Integer> excludeIds, List<Integer> checkList) {
        for (T child : treeList) {
            GetNeedLevelTreeNodeDTO(child, maxLevel, excludeIds, checkList);
        }

        return treeList;
    }

    /**
     * 排除需要删除的树节点后，只保留{maxLevel}级树节点；
     *
     * @param treeList  需要筛选的树结构数据列表
     * @param maxLevel 最大的节点深度
     * @param excludeIds 需要排除的树Id列表
     * @return
     */
    public static <T extends TreeNodeSimpleDTO<T>> List<T> LoadNeedLevelSimpleTreeNodeDTO(List<T> treeList, int maxLevel, List<Integer> excludeIds, List<Integer> checkList) {
        for (T child : treeList) {
            GetNeedLevelSimpleTreeNodeDTO(child, maxLevel, excludeIds, checkList);
        }

        return treeList;
    }
}
