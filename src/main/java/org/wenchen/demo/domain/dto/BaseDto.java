package org.wenchen.demo.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础Dto
 *
 * @author dejavu
 * @date 2020/6/1 16:12
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class BaseDto implements Serializable {

    private static final long serialVersionUID = 2985535678913619503L;

    private Long id;

    private Long creator;

    private LocalDateTime createTime;

    private LocalDateTime lastModifiedTime;

    private Integer version;

    public BaseDto(Long id) {
        this.id = id;
    }
}