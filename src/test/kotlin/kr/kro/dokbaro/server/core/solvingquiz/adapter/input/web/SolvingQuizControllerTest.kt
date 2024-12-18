package kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.SolveQuestionRequest
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.StartSolvingQuizRequest
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllMySolveSummaryUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllMyStudyGroupSolveSummaryUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllSolveResultUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.SolveQuestionUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.StartSolvingQuizUseCase
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.SolveResult
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.TotalGradeResult
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MySolvingQuizSortKeyword
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(SolvingQuizController::class)
class SolvingQuizControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var startSolvingQuizUseCase: StartSolvingQuizUseCase

	@MockkBean
	lateinit var solveQuestionUseCase: SolveQuestionUseCase

	@MockkBean
	lateinit var findAllSolveResultUseCase: FindAllSolveResultUseCase

	@MockkBean
	lateinit var findAllMySolveSummaryUseCase: FindAllMySolveSummaryUseCase

	@MockkBean
	lateinit var findAllMyStudyGroupSolveSummaryUseCase: FindAllMyStudyGroupSolveSummaryUseCase

	init {
		"퀴즈 풀기를 시작한다" {
			every { startSolvingQuizUseCase.start(any()) } returns 1

			val body = StartSolvingQuizRequest(1)

			performPost(Path("/solving-quiz/start"), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"solving-quiz/solving-quiz-start",
						requestFields(
							fieldWithPath("quizId").type(JsonFieldType.NUMBER).description("quiz id"),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("solving quiz id"),
						),
					),
				)
		}

		"퀴즈 문제에 대한 답을 체출한다" {
			every { solveQuestionUseCase.solve(any()) } returns
				SolveResult(
					solvingQuizId = 1L,
					playerId = 101L,
					quizId = 1001L,
					questionId = 10001L,
					correct = true,
					correctAnswer = listOf("A", "B"),
					answerExplanationContent = "The correct answers are A and B because...",
					answerExplanationImages = listOf("image1.jpg", "image2.jpg"),
				)

			val body = SolveQuestionRequest(1, listOf("홍길동, 가나다"))

			performPost(Path("/solving-quiz/{id}/sheets", "1"), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"solving-quiz/submit-quiz-sheet",
						pathParameters(
							parameterWithName("id").description("solving quiz id"),
						),
						requestFields(
							fieldWithPath("questionId").type(JsonFieldType.NUMBER).description("질문 id"),
							fieldWithPath("answers").type(JsonFieldType.ARRAY).description("답안"),
						),
						responseFields(
							fieldWithPath("solvingQuizId")
								.type(JsonFieldType.NUMBER)
								.description("현재 풀고 있는 퀴즈의 고유 ID."),
							fieldWithPath("playerId").type(JsonFieldType.NUMBER).description("플레이어의 ID."),
							fieldWithPath("quizId").type(JsonFieldType.NUMBER).description("퀴즈의 ID."),
							fieldWithPath("questionId").type(JsonFieldType.NUMBER).description("질문의 ID."),
							fieldWithPath("correct")
								.type(JsonFieldType.BOOLEAN)
								.description("정답 여부. 정답이면 true, 오답이면 false."),
							fieldWithPath("correctAnswer").type(JsonFieldType.ARRAY).description("정답으로 인정되는 답안의 목록."),
							fieldWithPath("answerExplanationContent")
								.type(JsonFieldType.STRING)
								.description("정답에 대한 설명 내용."),
							fieldWithPath("answerExplanationImages")
								.type(JsonFieldType.ARRAY)
								.description("정답 설명에 포함된 이미지 URL 목록."),
						),
					),
				)
		}

		"전체 채점 결과를 조회한다" {
			every { findAllSolveResultUseCase.findAllBy(any()) } returns
				TotalGradeResult(1, 1, 1, 10, 8)

			performGet(Path("/solving-quiz/{id}/grade-result", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"solving-quiz/grade-result",
						pathParameters(parameterWithName("id").description("문제 풀이 ID")),
						responseFields(
							fieldWithPath("solvingQuizId").type(JsonFieldType.NUMBER).description("풀고 있는 퀴즈의 고유 ID."),
							fieldWithPath("quizId").type(JsonFieldType.NUMBER).description("퀴즈의 ID."),
							fieldWithPath("playerId").type(JsonFieldType.NUMBER).description("플레이어의 ID."),
							fieldWithPath("questionCount").type(JsonFieldType.NUMBER).description("퀴즈에 포함된 질문의 총 개수."),
							fieldWithPath("correctCount").type(JsonFieldType.NUMBER).description("플레이어가 맞힌 정답의 개수."),
						),
					),
				)
		}

		"내가 푼 퀴즈 목록을 조회한다" {
			every { findAllMySolveSummaryUseCase.findAllMySolveSummary(any(), any()) } returns
				PageResponse.of(
					100,
					4,
					listOf(
						MySolveSummary(
							id = 1L,
							solvedAt = LocalDateTime.of(2024, 11, 8, 10, 0),
							bookImageUrl = "https://example.com/book1.jpg",
							quiz =
								MySolveSummary.Quiz(
									id = 101L,
									title = "Math Quiz",
								),
						),
						MySolveSummary(
							id = 2L,
							solvedAt = LocalDateTime.of(2024, 11, 8, 15, 30),
							bookImageUrl = "https://example.com/book2.jpg",
							quiz =
								MySolveSummary.Quiz(
									id = 102L,
									title = "Science Quiz",
								),
						),
					),
				)
			val params =
				mapOf(
					"page" to "1",
					"size" to "10",
					"sort" to MySolvingQuizSortKeyword.CREATED_AT.name,
					"direction" to SortDirection.ASC.name,
				)

			performGet(Path("/solving-quiz/my"), params)
				.andExpect(status().isOk)
				.andDo(
					print(
						"solving-quiz/my-solved",
						queryParameters(
							parameterWithName("page").description("결과 페이지 번호. 1부터 시작."),
							parameterWithName("size").description("페이지당 결과 수."),
							parameterWithName("sort").description("정렬 기준. [CREATED_AT, STAR_RATING]"),
							parameterWithName("direction").description("정렬 방향. 가능한 값은 'ASC' 또는 'DESC'"),
						),
						responseFields(
							fieldWithPath("endPageNumber").type(JsonFieldType.NUMBER).description("마지막 페이지 번호."),
							fieldWithPath("data[].id").description("문제 풀이 요약의 고유 식별자"),
							fieldWithPath("data[].solvedAt").description("문제를 푼 날짜와 시간"),
							fieldWithPath("data[].bookImageUrl").description("퀴즈와 관련된 책 이미지의 URL (optional)").optional(),
							fieldWithPath("data[].quiz").description("푼 퀴즈에 대한 요약 정보"),
							fieldWithPath("data[].quiz.id").description("퀴즈의 고유 식별자"),
							fieldWithPath("data[].quiz.title").description("퀴즈 제목"),
						),
					),
				)
		}

		"그룹 내 내가 푼 퀴즈 목록을 조회한다" {
			every { findAllMyStudyGroupSolveSummaryUseCase.findAllMyStudyGroupSolveSummary(any(), any()) } returns
				listOf(
					StudyGroupSolveSummary(
						id = 1L,
						solvedAt = LocalDateTime.of(2024, 11, 8, 10, 0),
						book =
							StudyGroupSolveSummary.Book(
								id = 1L,
								title = "Mathematics Essentials",
								imageUrl = "https://example.com/book1.jpg",
							),
						quiz =
							StudyGroupSolveSummary.Quiz(
								id = 101L,
								title = "Algebra Quiz",
								creator =
									StudyGroupSolveSummary.Creator(
										id = 1L,
										nickname = "MathGuru",
										profileImageUrl = "https://example.com/profile1.jpg",
									),
								createdAt = LocalDateTime.of(2024, 10, 1, 8, 30),
								contributors =
									listOf(
										StudyGroupSolveSummary.Contributor(
											id = 2L,
											nickname = "AlgebraAce",
											profileImageUrl = "https://example.com/profile2.jpg",
										),
										StudyGroupSolveSummary.Contributor(
											id = 3L,
											nickname = "GeometryGeek",
											profileImageUrl = null,
										),
									),
							),
					),
					StudyGroupSolveSummary(
						id = 2L,
						solvedAt = LocalDateTime.of(2024, 11, 8, 15, 30),
						book =
							StudyGroupSolveSummary.Book(
								id = 2L,
								title = "Science Basics",
								imageUrl = "https://example.com/book2.jpg",
							),
						quiz =
							StudyGroupSolveSummary.Quiz(
								id = 102L,
								title = "Physics Quiz",
								creator =
									StudyGroupSolveSummary.Creator(
										id = 2L,
										nickname = "ScienceSage",
										profileImageUrl = "https://example.com/profile3.jpg",
									),
								createdAt = LocalDateTime.of(2024, 9, 25, 14, 45),
								contributors =
									listOf(
										StudyGroupSolveSummary.Contributor(
											id = 4L,
											nickname = "PhysicsFan",
											profileImageUrl = "https://example.com/profile4.jpg",
										),
										StudyGroupSolveSummary.Contributor(
											id = 5L,
											nickname = "ChemistryChamp",
											profileImageUrl = "https://example.com/profile5.jpg",
										),
									),
							),
					),
				)

			performGet(Path("/solving-quiz/study-groups/{studyGroupId}/my", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"solving-quiz/study-group-my-solved",
						pathParameters(parameterWithName("studyGroupId").description("study group ID")),
						responseFields(
							fieldWithPath("[].id").description("문제 풀이 요약의 고유 식별자"),
							fieldWithPath("[].solvedAt").description("문제를 푼 날짜와 시간"),
							fieldWithPath("[].book").description("문제가 포함된 책에 대한 요약 정보"),
							fieldWithPath("[].book.id").description("책의 고유 식별자"),
							fieldWithPath("[].book.title").description("책 제목"),
							fieldWithPath("[].book.imageUrl").description("책 이미지의 URL"),
							fieldWithPath("[].quiz").description("푼 퀴즈에 대한 요약 정보"),
							fieldWithPath("[].quiz.id").description("퀴즈의 고유 식별자"),
							fieldWithPath("[].quiz.title").description("퀴즈 제목"),
							fieldWithPath("[].quiz.creator").description("퀴즈를 만든 사람 정보"),
							fieldWithPath("[].quiz.creator.id").description("퀴즈 작성자의 고유 식별자"),
							fieldWithPath("[].quiz.creator.nickname").description("퀴즈 작성자의 닉네임"),
							fieldWithPath("[].quiz.creator.profileImageUrl")
								.description("퀴즈 작성자의 프로필 이미지 URL (optional)")
								.optional(),
							fieldWithPath("[].quiz.createdAt").description("퀴즈가 생성된 날짜와 시간"),
							fieldWithPath("[].quiz.contributors").description("퀴즈 기여자 목록"),
							fieldWithPath("[].quiz.contributors[].id").description("퀴즈 기여자의 고유 식별자"),
							fieldWithPath("[].quiz.contributors[].nickname").description("퀴즈 기여자의 닉네임"),
							fieldWithPath("[].quiz.contributors[].profileImageUrl")
								.description("퀴즈 기여자의 프로필 이미지 URL (optional)")
								.optional(),
						),
					),
				)
		}
	}
}