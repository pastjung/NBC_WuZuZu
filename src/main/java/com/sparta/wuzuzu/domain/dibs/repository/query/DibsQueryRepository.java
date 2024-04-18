package com.sparta.wuzuzu.domain.dibs.repository.query;

import com.sparta.wuzuzu.domain.dibs.dto.DibsVo;
import java.util.List;

public interface DibsQueryRepository {
    List<DibsVo> findAllDibs(Long userId);
}
