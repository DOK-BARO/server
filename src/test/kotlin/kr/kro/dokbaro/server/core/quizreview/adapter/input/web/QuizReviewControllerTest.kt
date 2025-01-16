package kr.kro.dokbaro.server.core.quizreview.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto.CreateQuizReviewRequest
import kr.kro.dokbaro.server.core.quizreview.application.port.input.CreateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.DeleteQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindMyQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewSummaryUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewTotalScoreUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.UpdateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.query.MyQuizReview
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortKeyword
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewTotalScore
import kr.kro.dokbaro.server.fixture.adapter.input.web.endPageNumberFields
import kr.kro.dokbaro.server.fixture.adapter.input.web.pageQueryParameters
import kr.kro.dokbaro.server.fixture.adapter.input.web.pageRequestParams
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@WebMvcTest(QuizReviewController::class)
class QuizReviewControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createQuizReviewUseCase: CreateQuizReviewUseCase

	@MockkBean
	lateinit var findQuizReviewTotalScoreUseCase: FindQuizReviewTotalScoreUseCase

	@MockkBean
	lateinit var findQuizReviewSummaryUseCase: FindQuizReviewSummaryUseCase

	@MockkBean
	lateinit var updateQuizReviewUseCase: UpdateQuizReviewUseCase

	@MockkBean
	lateinit var deleteQuizReviewUseCase: DeleteQuizReviewUseCase

	@MockkBean
	lateinit var findMyQuizReviewUseCase: FindMyQuizReviewUseCase

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
							fieldWithPath("starRating")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈에 대한 점수. (예: 1에서 5 사이)"),
							fieldWithPath("difficultyLevel")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈의 난이도 수준. (예: 1에서 3 사이)"),
							fieldWithPath("comment")
								.type(JsonFieldType.STRING)
								.optional()
								.description("리뷰에 대한 추가적인 의견 (optional)"),
							fieldWithPath("quizId")
								.type(JsonFieldType.NUMBER)
								.description("리뷰하려는 퀴즈의 ID."),
						),
						responseFields(
							fieldWithPath("id")
								.type(JsonFieldType.NUMBER)
								.description("저장된 리뷰 ID"),
						),
					),
				)
		}

		"퀴즈 총 평점을 조회한다" {
			every { findQuizReviewTotalScoreUseCase.findTotalScoreBy(any()) } returns
				QuizReviewTotalScore(
					1,
					4.5,
					mapOf(
						1 to QuizReviewTotalScore.DifficultyScore(4, 0.5),
						2 to QuizReviewTotalScore.DifficultyScore(2, 0.33),
						3 to QuizReviewTotalScore.DifficultyScore(1, 0.17),
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
							fieldWithPath("averageStarRating")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈의 평균 점수. (optional)")
								.optional(),
							fieldWithPath("difficulty")
								.type(JsonFieldType.OBJECT)
								.description("난이도별 점수 총계를 포함한 맵.  (optional)")
								.optional(),
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

		"퀴즈 요약 목록을 조회한다" {
			every { findQuizReviewSummaryUseCase.findAllQuizReviewSummaryBy(any(), any()) } returns
				PageResponse(
					10,
					listOf(
						QuizReviewSummary(
							id = 1L,
							quizId = 101L,
							starRating = 5,
							difficultyLevel = 3,
							writerId = 1001L,
							writerNickname = "user123",
							comment = "This quiz was challenging but fun!",
							createdAt = Instant.parse("2024-10-20T10:15:30Z"),
							updatedAt = Instant.parse("2024-10-20T10:15:30Z"),
						),
						QuizReviewSummary(
							id = 2L,
							quizId = 102L,
							starRating = 4,
							difficultyLevel = 2,
							writerId = 1002L,
							writerNickname = "quizLover",
							comment = null,
							createdAt = Instant.parse("2024-10-21T14:20:00Z"),
							updatedAt = Instant.parse("2024-10-21T14:20:00Z"),
						),
					),
				)
			val params = pageRequestParams<QuizReviewSummarySortKeyword>(etc = mapOf("quizId" to "12345"))

			performGet(Path("/quiz-reviews"), params)
				.andExpect(status().isOk)
				.andDo(
					print(
						"quiz-review/find-all-summary",
						queryParameters(
							parameterWithName("quizId")
								.description("리뷰를 조회할 퀴즈의 ID."),
							*pageQueryParameters<QuizReviewSummarySortKeyword>(),
						),
						responseFields(
							endPageNumberFields(),
							fieldWithPath("data")
								.type(JsonFieldType.ARRAY)
								.description("퀴즈 리뷰 요약 목록."),
							fieldWithPath("data[].id")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈 리뷰의 ID."),
							fieldWithPath("data[].quizId")
								.type(JsonFieldType.NUMBER)
								.description("연관된 퀴즈의 ID."),
							fieldWithPath("data[].starRating")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈에 대한 별점 평가."),
							fieldWithPath("data[].difficultyLevel")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈의 난이도 수준."),
							fieldWithPath("data[].writerId")
								.type(JsonFieldType.NUMBER)
								.description("리뷰 작성자의 ID."),
							fieldWithPath("data[].writerNickname")
								.type(JsonFieldType.STRING)
								.description("리뷰 작성자의 닉네임."),
							fieldWithPath("data[].comment")
								.type(JsonFieldType.STRING)
								.optional()
								.description("리뷰에 대한 코멘트. (optional)"),
							fieldWithPath("data[].createdAt")
								.type(JsonFieldType.STRING)
								.description("리뷰 작성 시간. ISO 8601 포맷."),
							fieldWithPath("data[].updatedAt")
								.type(JsonFieldType.STRING)
								.description("리뷰 수정 시간. ISO 8601 포맷."),
						),
					),
				)
		}

		"퀴즈 후기를 수정한다" {
			every { updateQuizReviewUseCase.update(any()) } returns Unit

			val body =
				CreateQuizReviewRequest(
					starRating = 4,
					difficultyLevel = 3,
					comment = "퀴즈가 흥미롭고 잘 구성되어 있습니다.",
					quizId = 456L,
				)

			performPut(Path("/quiz-reviews/{id}", "1"), body)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"quiz-review/update",
						pathParameters(parameterWithName("id").description("리뷰 ID")),
						requestFields(
							fieldWithPath("starRating").description("퀴즈에 대한 별점 (1에서 5 사이의 값)"),
							fieldWithPath("difficultyLevel").description("퀴즈의 난이도 (1에서 3 사이의 값)"),
							fieldWithPath("comment").description("퀴즈에 대한 리뷰 내용").optional(),
							fieldWithPath("quizId").description("리뷰를 작성할 퀴즈의 고유 식별자"),
						),
					),
				)
		}

		"퀴즈 후기를 삭제한다" {
			every { deleteQuizReviewUseCase.delete(any()) } returns Unit

			performDelete(Path("/quiz-reviews/{id}", "1"))
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"quiz-review/delete",
						pathParameters(parameterWithName("id").description("리뷰 ID")),
					),
				)
		}

		"내가 작성한 퀴즈 리뷰를 조회한다" {
			every { findMyQuizReviewUseCase.findMyReviewBy(any(), any()) } returns
				MyQuizReview(
					id = 1L,
					starRating = 5,
					difficultyLevel = 3,
					comment = "This quiz was well-designed and challenging!",
					quizId = 101L,
				)

			val params = mapOf("quizId" to "1")
			performGet(Path("/quiz-reviews/my"), params)
				.andExpect(status().isOk)
				.andDo(
					print(
						"quiz-review/get-my-review",
						queryParameters(
							parameterWithName("quizId").description("퀴즈 ID"),
						),
						responseFields(
							fieldWithPath("id").description("리뷰의 고유 식별자."),
							fieldWithPath("starRating").description("별점."),
							fieldWithPath("difficultyLevel").description("퀴즈의 난이도"),
							fieldWithPath("comment").description("리뷰에 대한 사용자 코멘트. (optional)").optional(),
							fieldWithPath("quizId").description("리뷰가 연결된 퀴즈의 고유 식별자."),
						),
					),
				)
		}
	}
}