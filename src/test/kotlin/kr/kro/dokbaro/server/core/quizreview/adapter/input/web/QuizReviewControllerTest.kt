package kr.kro.dokbaro.server.core.quizreview.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto.CreateQuizReviewRequest
import kr.kro.dokbaro.server.core.quizreview.application.port.input.CreateQuizReviewUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(QuizReviewController::class)
class QuizReviewControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createQuizReviewUseCase: CreateQuizReviewUseCase

	init {
		"퀴즈 후기를 생성한다" {
			every { createQuizReviewUseCase.create(any()) } returns 1

			val body =
				CreateQuizReviewRequest(
					1,
					3,
					"엄청 어려워요",
					4,
				)

			performPost(Path("/quiz-reviews"), body)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"quiz-review/create",
						requestFields(
							fieldWithPath("score").type(JsonFieldType.NUMBER).description("퀴즈에 대한 점수. (예: 1에서 5 사이)"),
							fieldWithPath("difficultyLevel").type(JsonFieldType.NUMBER).description("퀴즈의 난이도 수준. (예: 1에서 3 사이)"),
							fieldWithPath("comment").type(JsonFieldType.STRING).optional().description("리뷰에 대한 추가적인 의견 (optional)"),
							fieldWithPath("quizId").type(JsonFieldType.NUMBER).description("리뷰하려는 퀴즈의 ID."),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("저장된 리뷰 ID"),
						),
					),
				)
		}
	}
}