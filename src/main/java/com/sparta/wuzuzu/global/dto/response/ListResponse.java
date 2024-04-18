package com.sparta.wuzuzu.global.dto.response;

import com.sparta.wuzuzu.global.util.PagingUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListResponse {

    private PagingUtil pagingUtil;
}
