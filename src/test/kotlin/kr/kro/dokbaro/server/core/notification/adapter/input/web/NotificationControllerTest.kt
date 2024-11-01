package kr.kro.dokbaro.server.core.notification.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.notification.application.port.input.CheckAllNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.DisableNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.FindAllNotificationUseCase
import kr.kro.dokbaro.server.core.notification.domain.NotificationTrigger
import kr.kro.dokbaro.server.core.notification.query.NotificationResult
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(NotificationController::class)
class NotificationControllerTest : RestDocsTest() {
	@MockkBean lateinit var checkAllNotificationUseCase: CheckAllNotificationUseCase

	@MockkBean lateinit var disableNotificationUseCase: DisableNotificationUseCase

	@MockkBean lateinit var findAllNotificationUseCase: FindAllNotificationUseCase

	init {

		"알림 체크를 수행한다" {
			every { checkAllNotificationUseCase.checkAll(any()) } returns Unit

			performPost(Path("/notifications/check"))
				.andExpect(status().isOk)
				.andDo(
					print("notification/check-all"),
				)
		}

		"알림 비활성화를 수행한다" {
			every { disableNotificationUseCase.disableBy(any(), any()) } returns Unit

			performPost(Path("/notifications/{id}/disable", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"notification/disable",
						pathParameters(parameterWithName("id").description("알림 ID")),
					),
				)
		}

		"알림 목록을 조회한다" {
			every { findAllNotificationUseCase.findAllBy(any()) } returns
				listOf(
					NotificationResult(
						content = "새로운 알림 내용입니다.",
						trigger = NotificationTrigger.UPDATE_QUIZ,
						imageUrl = "http://example.com/image.png",
						linkedId = 1234,
						checked = false,
						createdAt = LocalDateTime.now(),
					),
					NotificationResult(
						content = "새로운 알림 내용입니다.",
						trigger = NotificationTrigger.NEW_QUIZ_REVIEW,
						imageUrl = "http://example.com/image.png",
						linkedId = 1234,
						checked = true,
						createdAt = LocalDateTime.now(),
					),
				)

			performGet(Path("/notifications"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"notification/find-all",
						responseFields(
							fieldWithPath("[]").type(JsonFieldType.ARRAY).description("알림 결과의 리스트"),
							fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("알림의 고유 식별자"),
							fieldWithPath("[].content").type(JsonFieldType.STRING).description("알림의 내용"),
							fieldWithPath("[].trigger")
								.type(JsonFieldType.STRING)
								.description("알림의 발생 원인 (NEW_STUDY_GROUP_MEMBER, NEW_QUIZ, NEW_QUIZ_REVIEW, UPDATE_QUIZ,)"),
							fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("알림에 포함된 이미지 URL (optional)").optional(),
							fieldWithPath("[].linkedId").type(JsonFieldType.NUMBER).description("연결된 항목의 고유 ID (optional)").optional(),
							fieldWithPath("[].checked").type(JsonFieldType.BOOLEAN).description("알림 확인 여부"),
							fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("알림 생성 일시 (ISO-8601 형식)"),
						),
					),
				)
		}
	}
}