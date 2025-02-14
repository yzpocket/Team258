package com.example.team258.common.controller.mixedController;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.BookResponseLoadMoreDto;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.admin.service.AdminCategoriesService;
import com.example.team258.domain.bookSearch.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchMixedController {
    private final SearchService searchService;
    private final AdminCategoriesService adminCategoriesService;

    @GetMapping("/search")
    public String mySearchView(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "page", required = false) Integer page,
                               Model model) {
        long startTime = System.currentTimeMillis();//실행시간 측정
        model.addAttribute("categories", adminCategoriesService.getAllCategories());

        if (page == null) page = 1;
        if (bookCategoryName == null && keyword == null) {
            Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooks(page - 1);
            model.addAttribute("books", bookResponseDtoPage.getContent());
            model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());
        } else if (bookCategoryName == null && keyword != null) {
            Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooksByKeyword(keyword, page - 1);
            model.addAttribute("books", bookResponseDtoPage.getContent());
            model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());
        } else if (bookCategoryName != null && keyword == null) {
            Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooksByCategory(bookCategoryName, page - 1);
            model.addAttribute("books", bookResponseDtoPage.getContent());
            model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());
        } else if (bookCategoryName != null && keyword != null) {
            Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooksByCategoryOrKeyword(bookCategoryName, keyword, page - 1);
            model.addAttribute("books", bookResponseDtoPage.getContent());
            model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());
        }
        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "search";
    }

    @GetMapping("/search/v2")
    public String mySearchView2(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                               Model model) {
        long startTime = System.currentTimeMillis();//실행시간 측정
        model.addAttribute("categories", adminCategoriesService.getAllCategories());
        Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooksByCategoryOrKeyword2(bookCategoryName, keyword, page - 1);
        model.addAttribute("books", bookResponseDtoPage.getContent());
        model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());
        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "search";
    }

    @GetMapping("/search/v3")
    public String mySearchView3(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                Model model) {


        long startTime = System.currentTimeMillis();//실행시간 측정
        model.addAttribute("categories", adminCategoriesService.getAllCategories());
        Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooksByCategoryOrKeyword3(bookCategoryName, keyword, page - 1);
        model.addAttribute("books", bookResponseDtoPage.getContent());
        model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());

        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "search";
    }


    @GetMapping("/search/v4")
    public String mySearchView4(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                Model model) {

        // Slice로 변경
        Slice<BookResponseDto> bookResponseDtoSlice = searchService.getAllBooksByCategoryOrKeywordV4(bookCategoryName, keyword, page);


        long startTime = System.currentTimeMillis();//실행시간 측정
        model.addAttribute("categories", adminCategoriesService.getAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("books", bookResponseDtoSlice.getContent());
        model.addAttribute("hasNext", bookResponseDtoSlice.hasNext());

        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "users/searchV2";
    }


    // 더보기 기능 구현 초기 페이지 진입
    @GetMapping("/search/lm1")
    public String loadMoreResults(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                  Model model) {

        long startTime = System.currentTimeMillis(); // 실행시간 측정

        // 페이지 요청 시, 현재까지의 모든 페이지를 가져오도록 수정
        Slice<BookResponseDto> bookResponseDtoLoadMore = searchService.getMoreBooksByCategoryOrKeyword(bookCategoryName, keyword, page);

        model.addAttribute("categories", adminCategoriesService.getAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("books", bookResponseDtoLoadMore.getContent());
        model.addAttribute("hasNext", bookResponseDtoLoadMore.hasNext());

        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);


        return "users/searchLM1";
    }

    // 무한스크롤 기능 구현 초기 페이지 진입
    @GetMapping("/search/is1")
    public String infinityScrollResults(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                                        @RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                        Model model) {

        long startTime = System.currentTimeMillis(); // 실행시간 측정

        // 페이지 요청 시, 현재까지의 모든 페이지를 가져오도록 수정
        Slice<BookResponseDto> bookResponseDtoLoadMore = searchService.getMoreBooksByCategoryOrKeyword(bookCategoryName, keyword, page);

        model.addAttribute("categories", adminCategoriesService.getAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("books", bookResponseDtoLoadMore.getContent());
        model.addAttribute("hasNext", bookResponseDtoLoadMore.hasNext());


        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Object principal = securityContextHolder.getAuthentication().getPrincipal();
        String role = "";
        if(principal instanceof UserDetailsImpl)
            role = String.valueOf(((UserDetailsImpl) principal).getUser().getRole());
        else
            role = "ANONYMOUS";

        model.addAttribute("loginUserRole", role);

        return "users/searchIS1";
    }

    // 더보기 기능 구현 추가 페이지 로드
    @GetMapping("/search/loadMore")

    @ResponseBody
    public ResponseEntity<List<BookResponseLoadMoreDto>> loadMoreResults(
            @RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {

        long startTime = System.currentTimeMillis();


        Slice<BookResponseDto> bookResponseDtoLoadMore = searchService.getMoreBooksByCategoryOrKeyword(bookCategoryName, keyword, page);


        List<BookResponseLoadMoreDto> responseList = Collections.singletonList(
                new BookResponseLoadMoreDto(bookResponseDtoLoadMore.getContent())
        );

        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s");

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/search/fti")
    public String mySearchViewFTI(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                Model model) {

        long startTime = System.currentTimeMillis();//실행시간 측정
        // Slice로 변경
        Slice<BookResponseDto> bookResponseDtoSlice = searchService.getAllBooksByCategoryOrKeywordFTI(bookCategoryName,keyword,page-1);

        model.addAttribute("categories", adminCategoriesService.getAllCategories());
        model.addAttribute("currentPage", page);
        model.addAttribute("books", bookResponseDtoSlice.getContent());
        model.addAttribute("hasNext", bookResponseDtoSlice.hasNext());

        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        return "users/searchFTI";
    }

    @GetMapping("/search/loadMorefti")
    @ResponseBody
    public ResponseEntity<List<BookResponseLoadMoreDto>> loadMoreResultsFTI(
            @RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page) {

        long startTime = System.currentTimeMillis();

        Slice<BookResponseDto> bookResponseDtoLoadMore = searchService.getAllBooksByCategoryOrKeywordFTI(bookCategoryName, keyword, page);

        List<BookResponseLoadMoreDto> responseList = Collections.singletonList(
                new BookResponseLoadMoreDto(bookResponseDtoLoadMore.getContent())
        );

        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s");

        return ResponseEntity.ok(responseList);
    }

}
