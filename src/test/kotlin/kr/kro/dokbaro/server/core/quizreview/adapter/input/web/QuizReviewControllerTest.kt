package kr.kro.dokbaro.server.core.quizreview.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto.CreateQuizReviewRequest
import kr.kro.dokbaro.server.core.quizreview.application.port.input.CreateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewTotalScoreUseCase
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewTotalScore
import kr.kro.dokbaro.server.core.quizreview.query.TotalDifficultyScore
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(QuizReviewController::class)
class QuizReviewControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createQuizReviewUseCase: CreateQuizReviewUseCase

	@MockkBean
	lateinit var findQuizReviewTotalScoreUseCase: FindQuizReviewTotalScoreUseCase

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
							fieldWithPath("difficultyLevel")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈의 난이도 수준. (예: 1에서 3 사이)"),
							fieldWithPath("comment")
								.type(JsonFieldType.STRING)
								.optional()
								.description("리뷰에 대한 추가적인 의견 (optional)"),
							fieldWithPath("quizId").type(JsonFieldType.NUMBER).description("리뷰하려는 퀴즈의 ID."),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("저장된 리뷰 ID"),
						),
					),
				)
		}

		"퀴즈 총 평점을 조회한다" {
			every { findQuizReviewTotalScoreUseCase.findBy(any()) } returns
				QuizReviewTotalScore(
					1,
					4.5,
					mapOf(
						1 to TotalDifficultyScore(4, 0.5),
						2 to TotalDifficultyScore(2, 0.33),
						3 to TotalDifficultyScore(1, 0.17),
					),
				)
			val param = mapOf("quizId" to "1")

			performGet(Path("/quiz-reviews/total-score"), param = param)
				.andExpect(status().isOk)
				.andDo(
					print(
						"quiz-review/find-total-score",
						queryParameters(
							parameterWithName("quizId").description("퀴즈 ID"),
						),
						responseFields(
							fieldWithPath("quizId")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈의 ID."),
							fieldWithPath("averageScore")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈의 평균 점수."),
							fieldWithPath("difficulty")
								.type(JsonFieldType.OBJECT)
								.description("난이도별 점수 총계를 포함한 맵."),
							fieldWithPath("difficulty.*")
								.type(JsonFieldType.OBJECT)
								.description("각 난이도에 대한 점수 정보. {개수, 비율}"),
							fieldWithPath("difficulty.*.selectCount")
								.type(JsonFieldType.NUMBER)
								.description("해당 난이도를 선택한 횟수."),
							fieldWithPath("difficulty.*.selectRate")
								.type(JsonFieldType.NUMBER)
								.description("해당 난이도가 선택된 비율."),
						),
					),
				)
		}
	}
}