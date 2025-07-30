package kc.service.account;


import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.*;

import kc.dataaccess.account.*;
import kc.dto.ReportGroupDTO;
import kc.dto.account.UserTracingLogDTO;
import kc.framework.base.SeedEntity;
import kc.framework.extension.DateExtensions;
import kc.framework.security.MD5Provider;
import kc.framework.tenant.OrganizationConstants;
import kc.framework.tenant.RoleConstants;
import kc.mapping.account.UserTracingLogMapping;
import kc.model.account.*;
import kc.service.base.ServiceBase;
import kc.service.base.echarts.Option;
import kc.service.base.echarts.axis.CategoryAxis;
import kc.service.base.echarts.axis.ValueAxis;
import kc.service.base.echarts.code.AxisType;
import kc.service.base.echarts.code.LineType;
import kc.service.base.echarts.code.Symbol;
import kc.service.base.echarts.code.Trigger;
import kc.service.base.echarts.series.Line;
import kc.service.base.echarts.series.Series;
import kc.service.base.echarts.style.ItemStyle;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kc.dto.PaginatedBaseDTO;
import kc.dto.account.UserDTO;
import kc.dto.account.UserSimpleDTO;
import kc.framework.extension.StringExtensions;
import kc.mapping.account.UserMapping;

@Service
public class UserService extends ServiceBase implements IUserService {

    private final IUserRepository _userRepository;
    private final UserMapping _userMapping;

    private final IRoleRepository _RoleRepository;

    private final IOrganizationRepository _OrganizationRepository;

    private final IUserTracingLogRepository _userTracingLogRepository;
    private final UserTracingLogMapping _userTracingLogMapping;

    private final IUserLoginLogRepository _userLoginLogRepository;

    public UserService(IGlobalConfigApiService globalConfigApiService,
                       IUserRepository _userRepository,
                       UserMapping _userMapping,
                       IRoleRepository _RoleRepository,
                       IOrganizationRepository _OrganizationRepository,
                       IUserTracingLogRepository _userTracingLogRepository,
                       UserTracingLogMapping _userTracingLogMapping,
                       IUserLoginLogRepository _userLoginLogRepository) {
        super(globalConfigApiService);
        this._userRepository = _userRepository;
        this._userMapping = _userMapping;
        this._RoleRepository = _RoleRepository;
        this._OrganizationRepository = _OrganizationRepository;
        this._userTracingLogRepository = _userTracingLogRepository;
        this._userTracingLogMapping = _userTracingLogMapping;
        this._userLoginLogRepository = _userLoginLogRepository;
    }

    @Override
    public List<UserSimpleDTO> GetUserUsersByRoleIds(List<String> roleIds) {
        List<User> data = _userRepository.findUsersByRoleIds(roleIds);
        return _userMapping.simpleFrom(data);
    }

    @Override
    public List<UserDTO> GetAllUsersWithRolesAndOrgs() {
        List<User> data = _userRepository.FindAllDetailUsers();
        List<UserDTO> result = _userMapping.from(data);
        for (User user : data) {
            Optional<UserDTO> userOpt = result.stream().filter(u -> u.getUserId().equalsIgnoreCase(user.getId())).findFirst();
            if (userOpt.isPresent()) {
                userOpt.get().getRoleIds().addAll(user.getRoles().stream().map(r -> r.getId()).collect(Collectors.toList()));
                userOpt.get().getRoleNames().addAll(user.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList()));
                userOpt.get().getOrganizationIds().addAll(user.getOrganizations().stream().map(r -> r.getId()).collect(Collectors.toList()));
                //userOpt.get().getOrganizationNames().addAll(user.getOrganizations().stream().map(r -> r.getName()).collect(Collectors.toList()));
            }
        }

        return result;
    }

    @Override
    public PaginatedBaseDTO<UserDTO> findPaginatedUsersByFilter(int pageIndex, int pageSize,
                                                                String email, String phone, String name, Integer status, Integer position, Integer orgId, boolean isClient) {

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<User> data = _userRepository.findAll(new Specification<User>() {
            private static final long serialVersionUID = 924729964744553669L;

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringExtensions.isNullOrEmpty(email)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("email"), "%" + email + "%")));
                }
                if (!StringExtensions.isNullOrEmpty(phone)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("phoneNumber"), "%" + phone + "%")));
                }
                if (!StringExtensions.isNullOrEmpty(name)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("displayName"), "%" + name + "%")));
                }
                if (status != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), status)));
                }
                if (position != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("position"), position)));
                }
                if (orgId != null) {
                    //多对多的两张表关联查询：User：Organization  =  M：N
                    //方法一：在对象中定义好了多对多的关系，可以直接使用Join
                    Join<User, Organization> join = root.join("organizations", JoinType.INNER);  //可以用join方式联合查询，但是需要A中有B的实体。
                    predicates.add(criteriaBuilder.equal(join.get("id").as(Integer.class), orgId));

                    //方法二：使用SetJoin
                    //SetJoin<User, Organization> organizationsJoin = root.join(root.getModel().getSet("organizations", Organization.class), JoinType.LEFT);
                    //predicates.add(criteriaBuilder.equal(organizationsJoin.get("id").as(Integer.class), orgId));
                }
                if (isClient) {
                    predicates.add(criteriaBuilder.isTrue(root.get("client")));
                } else {
                    predicates.add(criteriaBuilder.isFalse(root.get("client")));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<UserDTO> rows = _userMapping.from(data.getContent());
        return new PaginatedBaseDTO<UserDTO>(pageIndex, pageSize, total, rows);
    }

    @Override
    public UserDTO findById(String userId) {
        User model = _userRepository.findById(userId);
        return _userMapping.from(model);
        //return new UserInfoMapper().Map(model);
    }

    @Override
    public UserSimpleDTO findSimpleUserWithOrgAndRoleNameByUserId(String userId) {
        User model = _userRepository.findWithOrgsAndRolesById(userId);
        UserSimpleDTO result = _userMapping.simpleFrom(model);
        result.setUserRoleIds(model.getRoles().stream().map(m -> m.getId()).collect(Collectors.toList()));
        result.setUserRoleNames(model.getRoles().stream().map(m -> m.getDisplayName()).collect(Collectors.toList()));
        result.setUserOrgIds(model.getOrganizations().stream().map(m -> m.getId()).collect(Collectors.toList()));
        result.setUserOrgNames(model.getOrganizations().stream().map(m -> m.getName()).collect(Collectors.toList()));
        return result;
    }

    @Override
    public boolean SaveUser(UserDTO model) {
        User data = _userMapping.to(model);
        data.setCreateDate(DateExtensions.getDateTimeUtcNow());
        if (model.isClient()) {
            SeedEntity seedEntity = getRegularDateVal("Customer", 1);
            data.setMemberId(seedEntity.getSeedValue());
            data.setUserName(model.getUserName() == null ? model.getPhoneNumber() : model.getUserName());
            data.setNormalizedUserName(model.getUserName() == null ? model.getPhoneNumber() : model.getUserName());

            Role cleintRole = _RoleRepository.findById(RoleConstants.ClientRoleId);
            if(cleintRole != null){
                cleintRole.getUsers().add(data);
                data.getRoles().add(cleintRole);
            }

            Organization clientOrg = _OrganizationRepository.findOneByOrganizationCode(OrganizationConstants.注册企业_Code);
            if(clientOrg != null){
                clientOrg.getUsers().add(data);
                data.getOrganizations().add(clientOrg);
            }
        } else {
            SeedEntity seedEntity = getRegularDateVal("Member", 1);
            data.setMemberId(seedEntity.getSeedValue());
            data.setNormalizedUserName(data.getNormalizedUserName());
        }
        if (!model.getPassword().isEmpty()) {
            try {
                data.setPasswordHash(MD5Provider.Hash(model.getPassword(), false));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return _userRepository.saveAndFlush(data) != null;
    }

    @Override
    public boolean RemoveUserById(Integer id) {
        _userRepository.deleteById(id);

        return true;
    }

    @Override
    public boolean UpdateClientById(Integer id) {
        Optional<User> optModel = _userRepository.findById(id);
        if (!optModel.isPresent())
            throw new IllegalArgumentException("与Id【\" + id + \"】相匹配的客户不存在,请重新输入！");

        User model = optModel.get();
        model.setClient(false);
        return _userRepository.saveAndFlush(model) != null;
    }

    @Override
    @Transactional
    public boolean UpdateRoleInUser(String userId, List<String> roleIds, String operatorId, String operatorName){
        User user = entityManager.createQuery("from User m LEFT OUTER JOIN FETCH m.roles r where m.id = :id", User.class)
                .setParameter("id", userId)
                .getSingleResult();

        if (user == null)
            throw new IllegalArgumentException(String.format("系统不存在用户Id=%s的用户。", userId));

        if (roleIds == null)
            throw new IllegalArgumentException("传入参数--roleIds：菜单Id列表为空。");

        List<String> dbIdList = user.getRoles().stream().map(role -> role.getId().toString()).collect(Collectors.toList());
        List<String> delList = dbIdList.stream().filter(m -> !roleIds.contains(m)).collect(Collectors.toList());
        List<String> addList = roleIds.stream().filter(m -> !dbIdList.contains(m)).collect(Collectors.toList());

        if (addList.size() == 0 && delList.size() == 0)
            return true;

        List<String> combinedList = java.util.stream.Stream.of(delList, addList)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        List<Role> allr = entityManager.createQuery("from Role r LEFT OUTER JOIN FETCH r.menuNodes m where r.id in (:ids)", Role.class)
                .setParameter("ids", combinedList)
                .getResultList();
        List<Role> delr = allr.stream()
                .filter(m -> delList.contains(m.getId()))
                .collect(Collectors.toList());
        for (Role role : delr)
        {
            role.getUsers().remove(user);
            if (delList.contains(role.getId()))
                user.getRoles().remove(role);
        }
        List<Role> addr = allr.stream()
                .filter(m -> addList.contains(m.getId()))
                .collect(Collectors.toList());
        for (Role role : addr)
        {
            role.getUsers().add(user);
            user.getRoles().add(role);
        }

        entityManager.merge(user);
        entityManager.flush();

        return true;
    }

    @Override
    public String ChangePassword(String userId, String OldPassword, String NewPassword) {
        PasswordEncoder passwordEncoder = new MD5PasswordEncoder();
        String oldPasswordHash = passwordEncoder.encode(OldPassword);
        String newPasswordHash = passwordEncoder.encode(NewPassword);

        User user = _userRepository.findById(userId);
        if (!user.getPasswordHash().equals(oldPasswordHash)) {
            return "当前密码不正确.";
        }

        user.setPasswordHash(newPasswordHash);
        _userRepository.save(user);
        return null;
    }

    @Override
    public String ChangeMailPhone(String userId, String email, String phone) {
        User user = _userRepository.findById(userId);
        if (!email.isEmpty())
            user.setEmail(email);
        if (!phone.isEmpty())
            user.setPhoneNumber(phone);
        _userRepository.save(user);

        return null;
    }

    @Override
    public boolean ExistUserName(String userName) {
        return _userRepository.existsByUserName(userName);
    }

    @Override
    public boolean ExistUserEmail(String email) {
        return _userRepository.existsByEmail(email);
    }

    @Override
    public boolean ExistUserPhone(String phone) {
        return _userRepository.existsByPhoneNumber(phone);
    }

    @Override
    public PaginatedBaseDTO<UserTracingLogDTO> findPaginatedUserLogsByName(int pageIndex, int pageSize, String name) {

        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        Page<UserTracingLog> data = _userTracingLogRepository.findAll(new Specification<UserTracingLog>() {
            @Override
            public Predicate toPredicate(Root<UserTracingLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!StringExtensions.isNullOrEmpty(name)) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("operator"), "%" + name + "%")));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        long total = data.getTotalElements();
        List<UserTracingLogDTO> rows = _userTracingLogMapping.from(data.getContent());
        return new PaginatedBaseDTO<UserTracingLogDTO>(pageIndex, pageSize, total, rows);
    }


    @Override
    public Option GetUserLoginReportData(Date startDate, Date endDate) {
        //获取所有的统计数据
        Date nowdate = new Date();
        Date startdate = startDate != null ? startDate : DateExtensions.addDay(nowdate, -5);
        Date enddate = endDate != null ? endDate : DateExtensions.addDay(nowdate, 5);

        //获取所有的统计数据
        List<UserLoginLog> data = _userLoginLogRepository.findAll(
                (Specification<UserLoginLog>) (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (startDate != null) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("OperateDate"), startdate)));
                    }
                    if (endDate != null) {
                        predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("OperateDate"), enddate)));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                });

        //查询数据进行统计分组
        String groupType = "登陆次数";
        List<String> legends = Arrays.asList(groupType);
        Map<String, List<UserLoginLog>> map = data.stream()
                .collect(Collectors.groupingBy(item -> new SimpleDateFormat("yyyy-MM-dd").format(item.getOperateDate())));
        List<ReportGroupDTO> groupData = new ArrayList<>();
        map.forEach((key, value) -> {
            ReportGroupDTO item = new ReportGroupDTO();
            item.setKey1(key);
            item.setKeyName1(groupType);
            item.setValue(BigDecimal.valueOf(value.size()));
            groupData.add(item);
        });

        //获取EChart所需的x轴数据
        List<String>  xData = DateExtensions.findAllDayStringsInDateDiff(startdate, enddate);
        //获取EChart所需的y轴数据
        List<Series> yData = new ArrayList<>();
        for (String line : legends) {
            List<BigDecimal> lineData = new ArrayList<>();
            for (String date : xData) {
                BigDecimal tempdata = groupData.stream()
                        .filter(m -> m.getKey1().equalsIgnoreCase(date) && m.getKeyName1().equalsIgnoreCase(line))
                        .map(ReportGroupDTO::getValue).reduce(BigDecimal.ZERO,BigDecimal::add);
                lineData.add(tempdata);
                //设置折线的样式
                ItemStyle style = new ItemStyle();
                style.normal().lineStyle().width(2).type(LineType.solid)
                        .color("rgba(30,144,255,0.8)").shadowColor("rgba(0,0,0,0.5)")
                        .shadowBlur(8).shadowOffsetX(8).shadowOffsetY(8);
                style.emphasis().label().show(true);
                //设置折线数据
                Line ydata = new Line(line).stack(line).symbol(Symbol.emptyCircle).symbolSize(6);
                ydata.setItemStyle(style);
                ydata.setData(lineData);
                yData.add(ydata);
            }
        }

        Option option = new Option();
        option.tooltip().trigger(Trigger.axis);
        option.legend().setData(legends);
        option.grid().y(40).x2(60).y2(40).x(60); //上、右、下、左

        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.type(AxisType.category);
        categoryAxis.boundaryGap(false);
        categoryAxis.setData(xData);
        option.xAxis(categoryAxis);

        ValueAxis valueAxis = new ValueAxis();
        valueAxis.type(AxisType.value);
        option.yAxis(valueAxis);
        option.setSeries(yData);

        return option;
    }
}
