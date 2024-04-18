package com.sparta.wuzuzu.domain.dibs.service;

import com.sparta.wuzuzu.domain.dibs.dto.DibsVo;
import com.sparta.wuzuzu.domain.dibs.entity.Dibs;
import com.sparta.wuzuzu.domain.dibs.repository.DibsRepository;
import com.sparta.wuzuzu.domain.dibs.repository.query.DibsQueryRepository;
import com.sparta.wuzuzu.domain.sale_post.repository.SalePostRepository;
import com.sparta.wuzuzu.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DibsService {
    private final DibsRepository dibsRepository;
    private final DibsQueryRepository dibsQueryRepository;
    private final SalePostRepository salePostRepository;

    public void createDibs(
        User user,
        Long salePostId
    ) {
        if(!salePostRepository.existsById(salePostId)){
            throw new IllegalArgumentException("존재하지 않는 페이지 입니다.");
        }

        if(dibsRepository.existsBySalePostId(salePostId)){
            throw new IllegalArgumentException("이미 존재하는 찜 목록 입니다.");
        }

        dibsRepository.save(new Dibs(salePostId, user));
    }

    public List<DibsVo> getDibs(User user) {
        List<DibsVo> dibsList = dibsQueryRepository.findAllDibs(user.getUserId());

        if (dibsList.isEmpty()) {
            throw new IllegalArgumentException("Dibs list is empty");
        }

        return dibsList;
    }

    public void deleteDibs(
        User user,
        Long salePostId
    ) {
        Dibs dibs = dibsRepository.findByUserAndSalePostId(user, salePostId);

        if(dibs == null){
            throw new IllegalArgumentException("Dibs is empty.");
        }

        dibsRepository.delete(dibs);
    }
}
