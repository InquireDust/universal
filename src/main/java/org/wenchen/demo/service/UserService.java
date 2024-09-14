package org.wenchen.demo.service;

import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wenchen.demo.domain.*;
import org.wenchen.demo.domain.dto.*;
import org.wenchen.demo.mapper.TableAMapper;
import org.wenchen.demo.mapper.UserMapper;

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

    public UserDTO get(Long id) {
        return userMapper.selectById(id).toDto();
    }

    public List<UserDTO> list() {
        return JoinWrappers.lambda(User.class)
                .selectAll(User.class)
                .selectAs(Address::getAddress, UserDTO::getAddress)
                .selectAs(Address::getCity, UserDTO::getCity)
                .leftJoin(Address.class, Address::getUserId, User::getId)
                .list(UserDTO.class);
    }

    public List<TableADTO> list2() {
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
}
