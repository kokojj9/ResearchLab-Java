package com.example.researchlab.investment.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.investment.model.service.InvestmentService;
import com.example.researchlab.investment.model.vo.MyStockList;
import com.example.researchlab.member.controller.MemberController;
import com.example.researchlab.member.model.vo.Member;
import com.example.researchlab.template.ResponseTemplate;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/investment")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;
    private final ResponseTemplate responseTemplate;
    private final MemberController memberController;

    @GetMapping(value = "/findStock", produces = "application/json; charset=UTF-8")
    public String getStockInfo(@RequestParam String stockName) throws IOException {
        // 여러날짜 데이터가 조회되어 가장 최근날짜 데이터만 필터링하는 로직은 프론트에서 구성함
        // 공공데이터 api는 2일전 종가로 조회 되기 때문에 추후에 다른 증권사api로 고도화를 통해 바꿀 예정
        String encodedStockName = URLEncoder.encode(stockName, "UTF-8");

        return investmentService.getStockInfo(encodedStockName);
    }

    @PostMapping("/saveList")
    public ResponseEntity<ResponseData<Object>> saveStockList(@RequestBody MyStockList stockList, HttpSession session) {
        // 사용자 설정 목록 저장 메소드
        Member loginMember = (Member) session.getAttribute("loginMember");
        List<MyStockList> list = new ArrayList<>();
        if (loginMember != null && stockList != null) {
            list = saveUserStock(stockList, loginMember);

            if (list != null) {
                return responseTemplate.success("리스트 저장 성공", list, HttpStatus.OK);
            }
        }

        return responseTemplate.fail("잘못된 요청", HttpStatus.BAD_REQUEST);
    }

    private List<MyStockList> saveUserStock(MyStockList stockList, Member loginMember) {
        HashMap<String, Object> userStockList = new HashMap<>();
        userStockList.put("memberNo", loginMember.getMemberNo());
        userStockList.put("stockList", stockList);
        return investmentService.saveStockList(userStockList);
    }


}
