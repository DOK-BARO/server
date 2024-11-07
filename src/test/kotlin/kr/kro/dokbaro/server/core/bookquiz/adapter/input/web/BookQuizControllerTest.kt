package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.CreateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.UpdateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.CreateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizAnswerUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizQuestionUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizSummaryUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindMyBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindUnsolvedGroupBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.UpdateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateQuizQuestionCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateQuizQuestionCommand
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummarySortOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookSummary
import kr.kro.dokbaro.server.core.bookquiz.query.Creator
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.Question
import kr.kro.dokbaro.server.core.bookquiz.query.QuizContributor
import kr.kro.dokbaro.server.core.bookquiz.query.QuizCreator
import kr.kro.dokbaro.server.core.bookquiz.query.QuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import kr.kro.dokbaro.server.fixture.domain.bookQuizAnswerFixture
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

@WebMvcTest(BookQuizController::class)
class BookQuizControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createBookQuizUseCase: CreateBookQuizUseCase

	@MockkBean
	lateinit var findBookQuizQuestionUseCase: FindBookQuizQuestionUseCase

	@MockkBean
	lateinit var updateBookQuizUseCase: UpdateBookQuizUseCase

	@MockkBean
	lateinit var findBookQuizAnswerUseCase: FindBookQuizAnswerUseCase

	@MockkBean
	lateinit var findBookQuizSummaryUseCase: FindBookQuizSummaryUseCase

	@MockkBean
	lateinit var findUnsolvedGroupBookQuizUseCase: FindUnsolvedGroupBookQuizUseCase

	@MockkBean
	lateinit var findMyBookQuizUseCase: FindMyBookQuizUseCase

	init {
		"북 퀴즈 생성을 수행한다" {
			every { createBookQuizUseCase.create(any()) } returns 1

			val body =
				CreateBookQuizRequest(
					title = "헥사고날 1장 요약",
					description = "문제가 쉬우니 모두 함께해요!",
					bookId = 56,
					questions =
						listOf(
							CreateQuizQuestionCommand(
								"다음 중 천만 관객 영화가 아닌 것은?",
								listOf(
									"광해",
									"명량",
									"암살",
									"국제시장",
									"자전차왕 엄복동",
								),
								"엄복동은 누적 관객 수 17만명을 기록했다.",
								listOf("hello.png"),
								QuizType.MULTIPLE_CHOICE,
								listOf("4"),
							),
							CreateQuizQuestionCommand(
								content = "명량에서 이순신 역은 류승룡이 담당했다",
								answerExplanationContent = "최민식이 담당했다",
								answerExplanationImages = listOf("hello.png"),
								answerType = QuizType.OX,
								answers = listOf("X"),
							),
						),
					studyGroupId = 2,
					timeLimitSecond = 60,
					viewScope = AccessScope.EVERYONE,
					editScope = AccessScope.CREATOR,
				)

			performPost(Path("/book-quizzes"), body)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"book-quiz/create-book-quiz",
						requestFields(
							fieldWithPath("title")
								.type(JsonFieldType.STRING)
								.description("퀴즈 제목"),
							fieldWithPath("description")
								.type(JsonFieldType.STRING)
								.description("퀴즈 설명"),
							fieldWithPath("bookId")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈 관련 서적 ID"),
							fieldWithPath("timeLimitSecond")
								.type(JsonFieldType.NUMBER)
								.description("풀이 제한 시간 (optional), 무제한 시 null")
								.optional(),
							fieldWithPath("viewScope")
								.type(JsonFieldType.STRING)
								.description("보기 접근 권한 [EVERYONE, STUDY_GROUP, CREATOR]"),
							fieldWithPath("editScope")
								.type(JsonFieldType.STRING)
								.description("편집 접근 권한 [EVERYONE, STUDY_GROUP, CREATOR]"),
							fieldWithPath("questions[].content")
								.type(JsonFieldType.STRING)
								.description("질문 내용"),
							fieldWithPath("questions[].selectOptions")
								.type(JsonFieldType.ARRAY)
								.description("질문 선택지, 객관식에서만 사용, seq는 0부터 시작"),
							fieldWithPath("questions[].answerExplanationContent")
								.type(JsonFieldType.STRING)
								.description("답안 설명"),
							fieldWithPath("questions[].answerExplanationImages")
								.type(JsonFieldType.ARRAY)
								.description("설명 사진 목록"),
							fieldWithPath("questions[].answerType")
								.type(JsonFieldType.STRING)
								.description("답안 타입 [OX, FILL_BLANK, MULTIPLE_CHOICE, SHORT]"),
							fieldWithPath("questions[].answers")
								.type(JsonFieldType.ARRAY)
								.description("답안"),
							fieldWithPath("studyGroupId")
								.type(JsonFieldType.NUMBER)
								.description("연관 Study group ID (optional)")
								.optional(),
						),
						responseFields(
							fieldWithPath("id")
								.type(JsonFieldType.NUMBER)
								.description("저장된 북퀴즈 ID"),
						),
					),
				)
		}

		"북 퀴즈 문제 조회를 수행한다" {
			every { findBookQuizQuestionUseCase.findBookQuizQuestionsBy(any()) } returns
				BookQuizQuestions(
					1,
					"java 정석 1차",
					60,
					listOf(
						Question(
							1,
							"조정석의 아내 이름은?",
							QuizType.MULTIPLE_CHOICE,
							listOf(
								SelectOption("거미"),
								SelectOption("개미"),
								SelectOption("고미"),
							),
						),
					),
				)

			performGet(Path("/book-quizzes/{id}/questions", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/get-book-quiz-questions",
						pathParameters(parameterWithName("id").description("퀴즈 ID")),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("퀴즈의 고유 식별자").optional(),
							fieldWithPath("title").type(JsonFieldType.STRING).description("퀴즈 제목"),
							fieldWithPath("timeLimitSecond")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈의 시간 제한(초), null 가능")
								.optional(),
							fieldWithPath("questions").type(JsonFieldType.ARRAY).description("퀴즈에 포함된 질문 리스트"),
							fieldWithPath("questions[].id").type(JsonFieldType.NUMBER).description("질문의 고유 식별자"),
							fieldWithPath("questions[].content").type(JsonFieldType.STRING).description("질문의 내용"),
							fieldWithPath("questions[].selectOptions")
								.type(JsonFieldType.ARRAY)
								.description("질문의 선택지 리스트"),
							fieldWithPath("questions[].type")
								.type(JsonFieldType.STRING)
								.description("질문의 유형 [OX, FILL_BLANK, MULTIPLE_CHOICE, SHORT]"),
							fieldWithPath("questions[].selectOptions[].content")
								.type(JsonFieldType.STRING)
								.description("선택지의 내용"),
						),
					),
				)
		}

		"퀴즈를 수정한다" {
			every { updateBookQuizUseCase.update(any()) } returns Unit

			val body =
				UpdateBookQuizRequest(
					title = "헥사고날 1장 요약",
					description = "문제가 쉬우니 모두 함께해요!",
					bookId = 56,
					questions =
						listOf(
							UpdateQuizQuestionCommand(
								id = 1,
								"다음 중 천만 관객 영화가 아닌 것은?",
								listOf(
									"광해",
									"명량",
									"암살",
									"국제시장",
									"자전차왕 엄복동",
								),
								"엄복동은 누적 관객 수 17만명을 기록했다.",
								listOf("hello.png", "world.jpg"),
								QuizType.MULTIPLE_CHOICE,
								listOf("4"),
							),
							UpdateQuizQuestionCommand(
								id = 2,
								content = "명량에서 이순신 역은 류승룡이 담당했다",
								answerExplanationContent = "최민식이 담당했다",
								answerType = QuizType.OX,
								answers = listOf("X"),
							),
						),
					studyGroupId = 2,
					timeLimitSecond = 60,
					viewScope = AccessScope.EVERYONE,
					editScope = AccessScope.CREATOR,
				)

			performPut(Path("/book-quizzes/{id}", "1"), body)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"book-quiz/update-book-quiz",
						pathParameters(parameterWithName("id").description("퀴즈 ID")),
						requestFields(
							fieldWithPath("title")
								.type(JsonFieldType.STRING)
								.description("퀴즈 제목"),
							fieldWithPath("description")
								.type(JsonFieldType.STRING)
								.description("퀴즈 설명"),
							fieldWithPath("bookId")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈 관련 서적 ID"),
							fieldWithPath("timeLimitSecond")
								.type(JsonFieldType.NUMBER)
								.description("풀이 제한 시간 (optional), 무제한 시 null")
								.optional(),
							fieldWithPath("viewScope")
								.type(JsonFieldType.STRING)
								.description("보기 접근 권한 [EVERYONE, STUDY_GROUP, CREATOR]"),
							fieldWithPath("editScope")
								.type(JsonFieldType.STRING)
								.description("편집 접근 권한 [EVERYONE, STUDY_GROUP, CREATOR]"),
							fieldWithPath("questions[].id")
								.type(JsonFieldType.NUMBER)
								.description("questionId, 신규 api 인 경우 null 혹은 0")
								.optional(),
							fieldWithPath("questions[].content")
								.type(JsonFieldType.STRING)
								.description("질문 내용"),
							fieldWithPath("questions[].selectOptions")
								.type(JsonFieldType.ARRAY)
								.description("질문 선택지, 객관식에서만 사용, seq는 0부터 시작"),
							fieldWithPath("questions[].answerExplanationContent")
								.type(JsonFieldType.STRING)
								.description("답안 설명"),
							fieldWithPath("questions[].answerExplanationImages")
								.type(JsonFieldType.ARRAY)
								.description("답안 설명 이미지 파일들"),
							fieldWithPath("questions[].answerType")
								.type(JsonFieldType.STRING)
								.description("답안 타입 [OX, FILL_BLANK, MULTIPLE_CHOICE, SHORT]"),
							fieldWithPath("questions[].answers")
								.type(JsonFieldType.ARRAY)
								.description("답안"),
							fieldWithPath("studyGroupId")
								.type(JsonFieldType.NUMBER)
								.description("연관 Study group ID (optional)")
								.optional(),
						),
					),
				)
		}

		"책에 대한 답변을 가져온다" {
			every { findBookQuizAnswerUseCase.findBookQuizAnswer(any()) } returns bookQuizAnswerFixture()

			val param = mapOf("questionId" to "1")
			performGet(Path("/book-quizzes/answer"), param)
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/get-answer",
						queryParameters(
							parameterWithName("questionId").description("질문 ID"),
						),
						responseFields(
							fieldWithPath("correctAnswer")
								.type(JsonFieldType.ARRAY)
								.description("정답 목록을 포함하는 배열. 여러 개의 정답이 있을 수 있다."),
							fieldWithPath("explanation").type(JsonFieldType.STRING).description("정답에 대한 설명."),
							fieldWithPath("explanationImages")
								.type(JsonFieldType.ARRAY)
								.description("설명 사진 목록"),
						),
					),
				)
		}

		"책에 대한 퀴즈 목록을 조회한다" {
			every { findBookQuizSummaryUseCase.findAllBookQuizSummary(any(), any(), any(), any(), any()) } returns
				PageResponse(
					endPageNumber = 5L,
					data =
						listOf(
							BookQuizSummary(
								id = 101L,
								title = "Kotlin Basics Quiz",
								averageStarRating = 4.5,
								averageDifficultyLevel = 3.2,
								questionCount = 10,
								creator =
									Creator(
										id = 1001L,
										nickname = "quizMaster01",
										profileUrl = "https://example.com/profiles/quizMaster01",
									),
							),
							BookQuizSummary(
								id = 102L,
								title = "Advanced Java Concepts",
								averageStarRating = 4.8,
								averageDifficultyLevel = 4.0,
								questionCount = 15,
								creator =
									Creator(
										id = 1002L,
										nickname = "javaExpert99",
										profileUrl = "https://example.com/profiles/javaExpert99",
									),
							),
							BookQuizSummary(
								id = 103L,
								title = "Python for Beginners",
								averageStarRating = 4.2,
								averageDifficultyLevel = 2.5,
								questionCount = 8,
								creator =
									Creator(
										id = 1003L,
										nickname = "pythonGuru",
										profileUrl = null,
									),
							),
						),
				)

			val params =
				mapOf(
					"page" to "1",
					"size" to "10",
					"bookId" to "12345",
					"sort" to BookQuizSummarySortOption.CREATED_AT.name,
					"direction" to SortDirection.ASC.name,
				)

			performGet(Path("/book-quizzes"), params)
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/get-summary",
						queryParameters(
							parameterWithName("page").description("결과 페이지 번호. 0부터 시작."),
							parameterWithName("size").description("페이지당 결과 수."),
							parameterWithName("bookId").description("퀴즈 목록을 조회할 책 ID."),
							parameterWithName("sort").description("정렬 기준. [CREATED_AT, STAR_RATING]"),
							parameterWithName("direction").description("정렬 방향. 가능한 값은 'ASC' 또는 'DESC'"),
						),
						responseFields(
							fieldWithPath("endPageNumber").type(JsonFieldType.NUMBER).description("마지막 페이지 번호."),
							fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("퀴즈의 ID."),
							fieldWithPath("data[].title").type(JsonFieldType.STRING).description("퀴즈의 제목."),
							fieldWithPath("data[].averageStarRating")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈의 평균 별점."),
							fieldWithPath("data[].averageDifficultyLevel")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈의 평균 난이도."),
							fieldWithPath("data[].questionCount").type(JsonFieldType.NUMBER).description("퀴즈의 질문 수."),
							fieldWithPath("data[].creator").type(JsonFieldType.OBJECT).description("퀴즈 작성자 정보."),
							fieldWithPath("data[].creator.id").type(JsonFieldType.NUMBER).description("작성자의 ID."),
							fieldWithPath("data[].creator.nickname")
								.type(JsonFieldType.STRING)
								.description("작성자의 닉네임."),
							fieldWithPath(
								"data[].creator.profileUrl",
							).type(JsonFieldType.STRING).optional().description("생성자의 프로필 URL. (optional)"),
						),
					),
				)
		}

		"스터디 그룹 퀴즈 중 본인이 안 푼 문제 목록을 조회한다" {
			every { findUnsolvedGroupBookQuizUseCase.findAllUnsolvedQuizzes(any(), any()) } returns
				listOf(
					UnsolvedGroupBookQuizSummary(
						book =
							BookSummary(
								id = 1L,
								title = "The Great Adventure",
								imageUrl = "https://example.com/the_great_adventure.jpg",
							),
						quiz =
							QuizSummary(
								id = 101L,
								title = "Adventure Quiz",
								creator =
									QuizCreator(
										id = 1001L,
										nickname = "quizMaster",
										profileImageUrl = "https://example.com/profile_quizmaster.jpg",
									),
								createdAt = LocalDateTime.of(2024, 5, 10, 14, 30, 0, 0),
								contributors =
									listOf(
										QuizContributor(
											id = 2001L,
											nickname = "contributorOne",
											profileImageUrl = "https://example.com/profile_contributorone.jpg",
										),
										QuizContributor(
											id = 2002L,
											nickname = "contributorTwo",
											profileImageUrl = "https://example.com/profile_contributortwo.jpg",
										),
									),
							),
					),
					UnsolvedGroupBookQuizSummary(
						book =
							BookSummary(
								id = 2L,
								title = "Mystery of the Lost City",
								imageUrl = "https://example.com/mystery_lost_city.jpg",
							),
						quiz =
							QuizSummary(
								id = 102L,
								title = "Mystery Quiz",
								creator =
									QuizCreator(
										id = 1002L,
										nickname = "mysterySolver",
										profileImageUrl = "https://example.com/profile_mysterysolver.jpg",
									),
								createdAt = LocalDateTime.of(2024, 6, 15, 10, 0, 0, 0),
								contributors =
									listOf(
										QuizContributor(
											id = 2003L,
											nickname = "mysteryFan",
											profileImageUrl = "https://example.com/profile_mysteryfan.jpg",
										),
									),
							),
					),
				)

			performGet(Path("/book-quizzes/study-groups/{studyGroupId}/unsolved", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/get-unsolved-study-group-quiz",
						pathParameters(parameterWithName("studyGroupId").description("스터디 그룹 ID")),
						responseFields(
							// Top level field
							fieldWithPath("[].book").description("퀴즈에 관련된 책 정보"),
							fieldWithPath("[].quiz").description("퀴즈의 상세 정보"),
							// Book summary fields
							fieldWithPath("[].book.id").description("책의 고유 ID"),
							fieldWithPath("[].book.title").description("책의 제목"),
							fieldWithPath("[].book.imageUrl").description("책의 이미지 URL"),
							// Quiz summary fields
							fieldWithPath("[].quiz.id").description("퀴즈의 고유 ID"),
							fieldWithPath("[].quiz.title").description("퀴즈 제목"),
							fieldWithPath("[].quiz.creator").description("퀴즈 생성자 정보"),
							fieldWithPath("[].quiz.createdAt").description("퀴즈 생성 날짜 및 시간"),
							fieldWithPath("[].quiz.contributors").description("퀴즈 기여자 목록"),
							// Quiz creator fields
							fieldWithPath("[].quiz.creator.id").description("퀴즈 생성자의 고유 ID"),
							fieldWithPath("[].quiz.creator.nickname").description("퀴즈 생성자의 닉네임"),
							fieldWithPath("[].quiz.creator.profileImageUrl")
								.description("퀴즈 생성자의 프로필 이미지 URL (선택 사항)"),
							// Quiz contributor fields (each contributor)
							fieldWithPath("[].quiz.contributors[].id").description("퀴즈 기여자의 고유 ID"),
							fieldWithPath("[].quiz.contributors[].nickname").description("퀴즈 기여자의 닉네임"),
							fieldWithPath("[].quiz.contributors[].profileImageUrl")
								.description("퀴즈 기여자의 프로필 이미지 URL (선택 사항)"),
						),
					),
				)
		}

		"내가 작성한 퀴즈 목록을 조회한다" {
			every { findMyBookQuizUseCase.findMyBookQuiz(any()) } returns
				listOf(
					MyBookQuizSummary(
						id = 1L,
						bookImageUrl = "https://example.com/book_image1.jpg",
						title = "Effective Kotlin",
						updatedAt = LocalDateTime.now().minusDays(2),
					),
					MyBookQuizSummary(
						id = 2L,
						bookImageUrl = "https://example.com/book_image2.jpg",
						title = "Kotlin in Action",
						updatedAt = LocalDateTime.now().minusDays(5),
					),
				)

			performGet(Path("/book-quizzes/my"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/get-my-quiz",
						responseFields(
							fieldWithPath("[].id").description("퀴즈 요약의 고유 ID"),
							fieldWithPath("[].bookImageUrl").optional().description("책의 이미지 URL (optional)"),
							fieldWithPath("[].title").description("책의 제목"),
							fieldWithPath("[].updatedAt").description("마지막으로 업데이트된 시간 (ISO 8601 형식)"),
						),
					),
				)
		}
	}
}