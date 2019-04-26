package com.xmcc.service;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.xmcc.beans.LogType;
import com.xmcc.dao.*;
import com.xmcc.dto.SysAclDto;
import com.xmcc.dto.SysAclLevelDto;
import com.xmcc.dto.SysDeptLevelDto;
import com.xmcc.model.*;
import com.xmcc.utils.IpUtil;
import com.xmcc.utils.JsonMapper;
import com.xmcc.utils.LevelUtils;
import com.xmcc.utils.RequestHolder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;


@Service
public class SysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclMapper sysAclMapper;
    @Resource
    private SysCoreServce sysCoreServce;
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;
    @Resource
    private SysLogMapper sysLogMapper;

    /**
     * 生成角色权限树
     */
    public List<SysAclLevelDto> RoleAclTree(int  roleId) {
        //获取所有的权限点
        List<SysAcl> allAclList = sysAclMapper.findAll();
        //通过刚刚写的CoreService来调用用户权限集合和角色权限集合
        List<SysAcl> RoleAclList = sysCoreServce.getAclListByRoleId(roleId);//角色权限集合
        List<SysAcl> UserAclList = sysCoreServce.getUserAclList();//用户权限集合
        //定义aclList集合来存储要显示的集合
        List<SysAclDto> aclList = new ArrayList<>();
        for (SysAcl sysAcl : allAclList) {
            //将该对象封装起来  这样他就拥有了 checked 和 hasacl属性
            SysAclDto sysAclDto = SysAclDto.adpter(sysAcl);
            //注意此时查看.contains方法的底层是equls方法比较的是对象的地址  所以这里每个对象地址是不一样的所以要重写Equals方法@EqualsAndHashCode(of = "id")
            //判断角色是否拥有这些属性
            if (RoleAclList.contains(sysAcl)) {
                sysAclDto.setChecked(true);
            }
            //判断用户是否有该权限
            if (UserAclList.contains(sysAcl)) {
                sysAclDto.setHasAcl(true);
            }
            aclList.add(sysAclDto);
        }
        return sysAclDtoToTree(aclList);
    }

    //封装权限模块树结构
    public List<SysAclLevelDto> sysAclDtoToTree(List<SysAclDto> aclList) {
        if (aclList == null) {
            return new ArrayList<>();
        }
        //将权限模点集合封装到权限模块树结构中
        //获取权限模块集合
        List<SysAclLevelDto> sysAclLevelDtos = aclTree();
        Multimap<Integer, SysAclDto> aclModuleAclMap = ArrayListMultimap.create();
        for (SysAclDto sysAclDto : aclList) {
            if (sysAclDto.getStatus() == 1) {
                aclModuleAclMap.put(sysAclDto.getAclModuleId(), sysAclDto);
            }
        }
        //递归生成权限点和权限树
        bingdAcls(sysAclLevelDtos, aclModuleAclMap);
        return sysAclLevelDtos;
    }

    //递归生成权限点和权限树
    public void bingdAcls(List<SysAclLevelDto> sysAclLevelDtos, Multimap<Integer, SysAclDto> aclMap) {
        if (sysAclLevelDtos == null) {
            return;
        }
        //遍历权限模块集合通过他的id来获取Map集合的对应的权限点
        for (SysAclLevelDto sysAclLevelDto : sysAclLevelDtos) {
            List<SysAclDto> sysAclDtoList = (List<SysAclDto>) aclMap.get(sysAclLevelDto.getId());
            //根据seq排序
            Collections.sort(sysAclDtoList, new MyComparator2());
            // 把当前的权限点集合存储到权限模块下
            sysAclLevelDto.setAclList(sysAclDtoList);
            //递归
            bingdAcls(sysAclLevelDto.getAclModuleList(), aclMap);
        }
    }

    //集合排序
    public class MyComparator2 implements Comparator<SysAclDto> {

        @Override
        public int compare(SysAclDto o1, SysAclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    }
    /**
     * 角色权限树更新
     *
     * @param roleId
     * @param aclIds
     */
    public void updateRoleAclTree(int roleId, List<Integer> aclIds) {
        if (aclIds.size() == 0) {
            return;
        }
        List<Integer> roleIdlistone = new ArrayList<>();
        roleIdlistone.add(roleId);
        //根据传过来的角色id查询出该角色原来拥有的权限id
        List<Integer> aclIdByRoleId = sysRoleAclMapper.findAclIdByRoleId(roleIdlistone);

        if (aclIdByRoleId.size() == aclIds.size()) {
            //比较内容
            //移除老集合中的元素
            aclIds.removeAll(roleIdlistone);
            if (aclIds.size() == 0) {
                //表示没有更新
                return;
            }
        }
        //删除aclIdByRoleId的相关数据更新list集合的表中的数据
        sysRoleAclMapper.deleteAclByRoleId(roleId);
        // 在按照newList里面的内容查询添加
        //准备数据重新插入
        List<SysRoleAcl> sysRoleAcls = new ArrayList<>();
        for (int i = 0; i < aclIds.size(); i++) {

            SysRoleAcl sysRoleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclIds.get(i))
                    .operateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler())).operateTime(new Date())
                    .operator(RequestHolder.getSysUserHodler().getUsername()).build();
            sysRoleAcls.add(sysRoleAcl);
        }
        //插入该对象
        sysRoleAclMapper.insertToNewList(sysRoleAcls);
        //日志记录
       saveRoleAclLog(roleId, aclIdByRoleId, aclIds);
    }
    //角色权限模块的日志记录
    public void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.SYS_ROLE_ACL);
        sysLogWithBLOBs.setTargetId(roleId);
        //如果是添加操作的话before是为空的
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperateIp(IpUtil.getUserIP(RequestHolder.getRequestHodler()));
        sysLogWithBLOBs.setOperator(RequestHolder.getSysUserHodler().getUsername());
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(0);
        sysLogMapper.insert(sysLogWithBLOBs);
    }
    /**
     * 生成部门树
     */
    public List<SysDeptLevelDto> deptTree() {
        //获取当前的所有的部门
        List<SysDept> sysDeptList = sysDeptMapper.findAll();
        //存储所有部门的dto
        List<SysDeptLevelDto> DeptLevelDtos = new ArrayList<>();
        //循环遍历当前的sysDeptList把sysdept存储到dto
        for (SysDept sysDept : sysDeptList) {
            //把当前获取的所有SysDept对象 转化为SysDeptLevelDto对象放到 List<SysDeptLevelDto> 中去
            SysDeptLevelDto sysDeptLevelDto = SysDeptLevelDto.adapter(sysDept);
            DeptLevelDtos.add(sysDeptLevelDto);

        }
        return deptDtoListToTree(DeptLevelDtos);
    }

    //封装dto
    public List<SysDeptLevelDto> deptDtoListToTree(List<SysDeptLevelDto> DeptLevelDtos) {
        //判断非空
        if (DeptLevelDtos == null) {
            return new ArrayList<SysDeptLevelDto>();
        }
        //创建集合存放顶层sysdeptdto
        List<SysDeptLevelDto> Rootdeptlist = new ArrayList<>();//0 层级
        //按照部门封装数据()
        /**
         * 注意  这里的Multimap<>,可以把相同key的数据的vlaue自动封装为list集合
         */
        Multimap<String, SysDeptLevelDto> map = ArrayListMultimap.create();//0.1层级的
        //遍历封装好的部门树 List<SysDeptLevelDto> 把顶级部门 存到Rootdeptlis集合中
        for (SysDeptLevelDto dtoTree : DeptLevelDtos) {
            if (dtoTree.getLevel().equals(LevelUtils.ROOT)) {
                Rootdeptlist.add(dtoTree);
            }
            //如果不是顶层集合就Muit
            map.put(dtoTree.getLevel(), dtoTree);

        }
        //部门根据seq排序
        Collections.sort(Rootdeptlist, new MyComparator());
        dGtoDeptTree(Rootdeptlist, map);
        return Rootdeptlist;
    }
    //递归生成树
    public void dGtoDeptTree(List<SysDeptLevelDto> Rootdeptlist, Multimap<String, SysDeptLevelDto> map) {
        //遍历顶层的每一个元素
        for (int i = 0; i < Rootdeptlist.size(); i++) {
            //获取到顶层集合的第一个dept对象
            SysDeptLevelDto deptLevelDto = Rootdeptlist.get(i);
            //获取他的下一个子部门的Level
            /**
             *这里因为他的子部门的level = 父亲的Levl .父亲的Id
             * 在这里我们取到的deptLevelDto就是它的父亲
             */
            String nextLevel = LevelUtils.calculate(deptLevelDto.getId(), deptLevelDto.getLevel());
            //处理下一层数据
            List<SysDeptLevelDto> sysDeptLevelDtos = (List<SysDeptLevelDto>) map.get(nextLevel);

            if (sysDeptLevelDtos != null) {
                //部门根据seq排序
                Collections.sort(sysDeptLevelDtos, new MyComparator());
                //设置下一层部门(这不操作就是把子部门的集合存到父部门对象下面，形成树结构)
                deptLevelDto.setDeptList(sysDeptLevelDtos);
                //然后继续递归直到子部门集合为空为止
                dGtoDeptTree(sysDeptLevelDtos, map);
            }

        }
    }

    //集合排序
    public class MyComparator implements Comparator<SysDeptLevelDto> {

        @Override
        public int compare(SysDeptLevelDto o1, SysDeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    }
    /**
     * 生成权限模块树
     */
    public List<SysAclLevelDto> aclTree() {
        //先获取所有的权限模块表
        List<SysAclModule> sysAclModules = sysAclModuleMapper.findAll();
        //存储所有权限模块的dto
        List<SysAclLevelDto> sysAclLevelDtos = new ArrayList<>();
        //循环遍历当前的sysAclModules把SysAclModule存储到sysAclLevelDtos
        for (SysAclModule s : sysAclModules) {
            //封装变成SysAclLevelDto类型的
            SysAclLevelDto aclTree = SysAclLevelDto.adapter(s);
//        存入集合
            sysAclLevelDtos.add(aclTree);
        }


        return aclListToTree(sysAclLevelDtos);
    }

    //把权限集合封装成树结构
    public List<SysAclLevelDto> aclListToTree(List<SysAclLevelDto> sysAclLevelDtos) {
        //判断非空
        if (sysAclLevelDtos == null) {
            return new ArrayList<SysAclLevelDto>();
        }
        //创建一个集合来存贮顶级权限
        List<SysAclLevelDto> RootList = new ArrayList<>();
        //创建一个map集合来存贮下层权限
        Multimap<String, SysAclLevelDto> map = ArrayListMultimap.create();
        //遍历封装好的部门树 List<SysAclLevelDto>把顶级部门 存到Rootlis集合中
        for (SysAclLevelDto sysAclLevelDto : sysAclLevelDtos) {
            //判断是不是顶级部门
            if (sysAclLevelDto.getLevel().equals(LevelUtils.ROOT)) {
                RootList.add(sysAclLevelDto);
            }
            //把所有的权限模块都存在map里面
            map.put(sysAclLevelDto.getLevel(), sysAclLevelDto);
        }

        //权限根据seq排序
        Collections.sort(RootList, new MyComparator1());
        //递归生成树结构
        dGtoAclTree(RootList, map);
        return RootList;
    }

    //权限集合排序
    public class MyComparator1 implements Comparator<SysAclLevelDto> {
        @Override
        public int compare(SysAclLevelDto o1, SysAclLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    }

    //递归生产权限模块树
    public void dGtoAclTree(List<SysAclLevelDto> RootList, Multimap<String, SysAclLevelDto> map) {
        //遍历顶层集合
        for (int i = 0; i < RootList.size(); i++) {
            //获取他的每个元素对象
            SysAclLevelDto sysAclLevelDto = RootList.get(i);
            //获取他的子部门的level
            String nextlevel = LevelUtils.calculate(sysAclLevelDto.getId(), sysAclLevelDto.getLevel());
            //然后通过这个level去map集合中去查找同一层的集合对象
            List<SysAclLevelDto> chlidAclList = (List<SysAclLevelDto>) map.get(nextlevel);
            if (chlidAclList != null) {
                //根据seq给子集和排序
                Collections.sort(chlidAclList, new MyComparator1());

                //把该子集和存入到他的父亲对象里面去
                sysAclLevelDto.setAclModuleList(chlidAclList);

                //递归
                dGtoAclTree(chlidAclList, map);
            }
        }
    }



}
