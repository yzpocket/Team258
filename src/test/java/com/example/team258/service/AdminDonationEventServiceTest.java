package com.example.team258.service;

import com.example.team258.dto.BookDonationEventRequestDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.BookDonationEvent;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.jwt.SecurityUtil;
import com.example.team258.repository.AdminDonationEventRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
class AdminDonationEventServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminDonationEventRepository adminDonationEventRepository;

    @MockBean
    private SecurityUtil securityUtil;

    @Autowired
    private AdminDonationEventService adminDonationEventService;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;
    @BeforeAll
    static void setUp() {
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }
    @AfterAll
    static void tearDown() {
        mockedSecurityUtil.close();
    }

    @Test
    void createDonationEvent() {
        // given
        MessageDto msg =  MessageDto.builder()
                .msg("이벤트추가가 완료되었습니다")
                .build();

        when(adminDonationEventRepository.save(any(BookDonationEvent.class))).thenReturn(new BookDonationEvent());

        // when
        ResponseEntity<MessageDto> result = adminDonationEventService.createDonationEvent(new BookDonationEventRequestDto());

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getMsg()).isEqualTo("이벤트추가가 완료되었습니다");

    }

    @Test
    void updateDonationEvent() {
        // given

        when(adminDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.of(new BookDonationEvent()));

        // when
        ResponseEntity<MessageDto> result = adminDonationEventService.updateDonationEvent(1L,new BookDonationEventRequestDto());

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getMsg()).isEqualTo("이벤트 수정이 완료되었습니다");
    }

    @Test
    void updateDonationEvent_Event_is_Null() {
        // given
        when(adminDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(IllegalArgumentException.class,
                () -> adminDonationEventService.updateDonationEvent(1L,new BookDonationEventRequestDto()));
    }


    @Test
    void deleteDonationEvent() {
        // given
        User user = User.builder()
                .role(UserRoleEnum.ADMIN)
                .build();

        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
        when(adminDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.of(new BookDonationEvent()));
        doNothing().when(adminDonationEventRepository).delete(any(BookDonationEvent.class));

        // when
        ResponseEntity<MessageDto> result = adminDonationEventService.deleteDonationEvent(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getMsg()).isEqualTo("이벤트 삭제가 완료되었습니다");
    }

    @Test
    void deleteDonationEvent_No_Event() {
        // given
        User user = User.builder()
                .role(UserRoleEnum.ADMIN)
                .build();

        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
        when(adminDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        doNothing().when(adminDonationEventRepository).delete(any(BookDonationEvent.class));

        // when
        // then
        assertThrows(IllegalArgumentException.class,()->adminDonationEventService.deleteDonationEvent(1L));
    }

    @Test
    void deleteDonationEvent_No_Admin() {
        // given
        User user = User.builder()
                .role(UserRoleEnum.USER)
                .build();

        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
        when(adminDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.of(new BookDonationEvent()));
        doNothing().when(adminDonationEventRepository).delete(any(BookDonationEvent.class));

        // when
        ResponseEntity<MessageDto> result = adminDonationEventService.deleteDonationEvent(1L);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMsg()).isEqualTo("관리자만 이벤트를 삭제할 수 있습니다.");
    }
}