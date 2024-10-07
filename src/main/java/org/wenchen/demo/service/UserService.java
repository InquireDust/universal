package org.wenchen.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wenchen.demo.domain.*;
import org.wenchen.demo.domain.dto.*;
import org.wenchen.demo.mapper.mpMapper.TableAMapper;
import org.wenchen.demo.mapper.mpMapper.TreeTableMapper;
import org.wenchen.demo.mapper.mpMapper.UserMapper;

import java.util.List;

/**
 * Author: wen-chen
 * Date: 2024/9/15
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final TableAMapper tableAMapper;
    private final TreeTableMapper treeTableMapper;

    public UserDTO get(Long id) {
        return userMapper.selectById(id).toDto();
    }

    public List<UserDTO> joinList() {
        return JoinWrappers.lambda(User.class)
                .selectAll(User.class)
                .selectAs(Address::getAddress, UserDTO::getAddress)
                .selectAs(Address::getCity, UserDTO::getCity)
                .leftJoin(Address.class, Address::getUserId, User::getId)
                .list(UserDTO.class);
    }

    public List<TableADTO> selectCollection() {
        MPJLambdaWrapper<TableA> wrapper = new MPJLambdaWrapper<TableA>()
                .selectAll(TableA.class)
                .selectCollection(TableB.class, TableADTO::getBList, b -> b
                        .collection(TableC.class, TableBDTO::getCList, c -> c
                                .collection(TableD.class, TableCDTO::getDList, d -> d
                                        .collection(TableE.class, TableDDTO::getEList))))
                .leftJoin(TableB.class, TableB::getAid, TableA::getId)
                .leftJoin(TableC.class, TableC::getBid, TableB::getId)
                .leftJoin(TableD.class, TableD::getCid, TableC::getId)
                .leftJoin(TableE.class, TableE::getDid, TableD::getId);

        List<TableADTO> dtos = tableAMapper.selectJoinList(TableADTO.class, wrapper);
        System.out.println(dtos);
        return dtos;
    }

    public UserDTO selectUserWithAddress() {
        return userMapper.selectUserWithAddress(1L);
    }

    public UserDTO test() {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(true,User::getId, 1L)
                .eq(true,User::getName,"wenchen")
                .eq(true,User::getAge,18);
        List<User> list = ChainWrappers.lambdaQueryChain(userMapper)
                .list();
        return userMapper.selectOne(userLambdaQueryWrapper).toDto();
    }

    public List<TreeTable> noteTree() {
        return treeTableMapper.noteTree();
    }
}
