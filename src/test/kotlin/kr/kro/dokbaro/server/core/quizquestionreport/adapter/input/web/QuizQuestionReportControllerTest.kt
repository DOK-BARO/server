package kr.kro.dokbaro.server.core.quizquestionreport.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.quizquestionreport.adapter.input.web.dto.CreateQuizQuestionReportRequest
import kr.kro.dokbaro.server.core.quizquestionreport.application.port.input.CreateQuizQuestionReportUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(QuizQuestionReportController::class)
class QuizQuestionReportControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createQuizQuestionReportUseCase: CreateQuizQuestionReportUseCase

	init {
		"북 퀴즈 질문을 신고한다" {
			every { createQuizQuestionReportUseCase.create(any()) } returns 1

			val body =
				CreateQuizQuestionReportRequest(
					questionId = 1,
					content = "너무 글이 안멋져요.",
				)

			performPost(Path("/quiz-question-reports"), body)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"quiz-question-report/create",
						requestFields(
							fieldWithPath("questionId").type(JsonFieldType.NUMBER).description("신고할 퀴즈 문제의 ID"),
							fieldWithPath("content").type(JsonFieldType.STRING).description("신고 내용"),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 퀴즈 문제 신고의 ID"),
						),
					),
				)
		}
	}
}