package kr.kro.dokbaro.server.core.quizreviewreport.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.quizreviewreport.adapter.input.web.dto.CreateQuizReviewReportRequest
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.CreateQuizReviewReportUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(QuizReviewReportController::class)
class QuizReviewReportControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createQuizReviewReportUseCase: CreateQuizReviewReportUseCase

	init {
		"퀴즈 리뷰를 신고한다" {
			every { createQuizReviewReportUseCase.create(any()) } returns 1

			val body =
				CreateQuizReviewReportRequest(
					quizReviewId = 123L,
					content = "이 리뷰는 부적절한 언어를 사용하고 있습니다.",
				)

			performPost(Path("/quiz-review-reports"), body)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"quiz-review-report/create",
						requestFields(
							fieldWithPath("quizReviewId").type(JsonFieldType.NUMBER).description("신고할 퀴즈 리뷰의 고유 식별자"),
							fieldWithPath("content").type(JsonFieldType.STRING).description("신고 내용"),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 신고 ID"),
						),
					),
				)
		}
	}
}