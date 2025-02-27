package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.CreateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.UpdateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.CreateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.DeleteBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizAnswerUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizDetailUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizExplanationUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizQuestionUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizSummaryUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindMyBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindUnsolvedGroupBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.UpdateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizDetail
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizExplanation
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.sort.BookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.MyBookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.UnsolvedGroupBookQuizSortKeyword
import kr.kro.dokbaro.server.fixture.adapter.input.web.endPageNumberFields
import kr.kro.dokbaro.server.fixture.adapter.input.web.pageQueryParameters
import kr.kro.dokbaro.server.fixture.adapter.input.web.pageRequestParams
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

	@MockkBean
	lateinit var deleteBookQuizUseCase: DeleteBookQuizUseCase

	@MockkBean
	lateinit var bookQuizExplanationUseCase: FindBookQuizExplanationUseCase

	@MockkBean
	lateinit var findBookQuizDetailUseCase: FindBookQuizDetailUseCase

	init {
		"북 퀴즈 생성을 수행한다" {
			every { createBookQuizUseCase.create(any(), any()) } returns 1

			val body =
				CreateBookQuizRequest(
					title = "헥사고날 1장 요약",
					description = "문제가 쉬우니 모두 함께해요!",
					bookId = 56,
					questions =
						listOf(
							CreateBookQuizCommand.Question(
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
								QuizType.MULTIPLE_CHOICE_MULTIPLE_ANSWER,
								listOf("4"),
							),
							CreateBookQuizCommand.Question(
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
								.description("답안 타입 [OX, FILL_BLANK, MULTIPLE_CHOICE_SINGLE_ANSWER,MULTIPLE_CHOICE_MULTIPLE_ANSWER, SHORT]"),
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
					10,
					listOf(
						BookQuizQuestions.Question(
							1,
							"조정석의 아내 이름은?",
							QuizType.MULTIPLE_CHOICE_MULTIPLE_ANSWER,
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
							fieldWithPath("studyGroupId")
								.type(JsonFieldType.NUMBER)
								.description("study group ID), null 가능")
								.optional(),
							fieldWithPath("questions").type(JsonFieldType.ARRAY).description("퀴즈에 포함된 질문 리스트"),
							fieldWithPath("questions[].id").type(JsonFieldType.NUMBER).description("질문의 고유 식별자"),
							fieldWithPath("questions[].content").type(JsonFieldType.STRING).description("질문의 내용"),
							fieldWithPath("questions[].selectOptions")
								.type(JsonFieldType.ARRAY)
								.description("질문의 선택지 리스트"),
							fieldWithPath("questions[].type")
								.type(JsonFieldType.STRING)
								.description("질문의 유형 [OX, FILL_BLANK, MULTIPLE_CHOICE_MULTIPLE_ANSWER, MULTIPLE_CHOICE_SINGLE_ANSWER, SHORT]"),
							fieldWithPath("questions[].selectOptions[].content")
								.type(JsonFieldType.STRING)
								.description("선택지의 내용"),
						),
					),
				)
		}

		"퀴즈를 수정한다" {
			every { updateBookQuizUseCase.update(any(), any()) } returns Unit

			val body =
				UpdateBookQuizRequest(
					title = "헥사고날 1장 요약",
					description = "문제가 쉬우니 모두 함께해요!",
					bookId = 56,
					questions =
						listOf(
							UpdateBookQuizCommand.Question(
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
								QuizType.MULTIPLE_CHOICE_MULTIPLE_ANSWER,
								listOf("4"),
							),
							UpdateBookQuizCommand.Question(
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
					temporary = true,
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
								.description("답안 타입 [OX, FILL_BLANK, MULTIPLE_CHOICE_MULTIPLE_ANSWER, MULTIPLE_CHOICE_SINGLE_ANSWER, SHORT]"),
							fieldWithPath("questions[].answers")
								.type(JsonFieldType.ARRAY)
								.description("답안"),
							fieldWithPath("studyGroupId")
								.type(JsonFieldType.NUMBER)
								.description("연관 Study group ID (optional)")
								.optional(),
							fieldWithPath("temporary")
								.type(JsonFieldType.BOOLEAN)
								.description("임시 저장 여부"),
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
			every { findBookQuizSummaryUseCase.findAllBookQuizSummary(any(), any()) } returns
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
								reviewCount = 20,
								creator =
									BookQuizSummary.Creator(
										id = 1001L,
										nickname = "quizMaster01",
										profileUrl = "https://example.com/profiles/quizMaster01",
									),
								temporary = true,
							),
							BookQuizSummary(
								id = 102L,
								title = "Advanced Java Concepts",
								averageStarRating = 4.8,
								averageDifficultyLevel = 4.0,
								questionCount = 15,
								reviewCount = 20,
								creator =
									BookQuizSummary.Creator(
										id = 1002L,
										nickname = "javaExpert99",
										profileUrl = "https://example.com/profiles/javaExpert99",
									),
								temporary = true,
							),
							BookQuizSummary(
								id = 103L,
								title = "Python for Beginners",
								averageStarRating = 4.2,
								averageDifficultyLevel = 2.5,
								questionCount = 8,
								reviewCount = 20,
								creator =
									BookQuizSummary.Creator(
										id = 1003L,
										nickname = "pythonGuru",
										profileUrl = null,
									),
								temporary = true,
							),
						),
				)

			val params = pageRequestParams<BookQuizSummarySortKeyword>(etc = mapOf("bookId" to "1"))

			performGet(Path("/book-quizzes"), params)
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/get-summary",
						queryParameters(
							parameterWithName("bookId").description("퀴즈 목록을 조회할 책 ID."),
							*pageQueryParameters<BookQuizSummarySortKeyword>(),
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
							fieldWithPath("data[].reviewCount").type(JsonFieldType.NUMBER).description("퀴즈의 리뷰 수."),
							fieldWithPath("data[].creator").type(JsonFieldType.OBJECT).description("퀴즈 작성자 정보."),
							fieldWithPath("data[].creator.id").type(JsonFieldType.NUMBER).description("작성자의 ID."),
							fieldWithPath("data[].creator.nickname")
								.type(JsonFieldType.STRING)
								.description("작성자의 닉네임."),
							fieldWithPath(
								"data[].creator.profileUrl",
							).type(JsonFieldType.STRING).optional().description("생성자의 프로필 URL. (optional)"),
							fieldWithPath("data[].temporary")
								.type(JsonFieldType.BOOLEAN)
								.description("임시 저장 여부"),
						),
					),
				)
		}

		"스터디 그룹 퀴즈 중 본인이 안 푼 문제 목록을 조회한다" {
			every { findUnsolvedGroupBookQuizUseCase.findAllUnsolvedQuizzes(any(), any(), any()) } returns
				PageResponse.of(
					1000,
					1,
					listOf(
						UnsolvedGroupBookQuizSummary(
							book =
								UnsolvedGroupBookQuizSummary.Book(
									id = 1L,
									title = "The Great Adventure",
									imageUrl = "https://example.com/the_great_adventure.jpg",
								),
							quiz =
								UnsolvedGroupBookQuizSummary.Quiz(
									id = 101L,
									title = "Adventure Quiz",
									creator =
										UnsolvedGroupBookQuizSummary.Creator(
											id = 1001L,
											nickname = "quizMaster",
											profileImageUrl = "https://example.com/profile_quizmaster.jpg",
										),
									description = "description",
									createdAt = LocalDateTime.of(2024, 5, 10, 14, 30, 0, 0),
									contributors =
										listOf(
											UnsolvedGroupBookQuizSummary.Contributor(
												id = 2001L,
												nickname = "contributorOne",
												profileImageUrl = "https://example.com/profile_contributorone.jpg",
											),
											UnsolvedGroupBookQuizSummary.Contributor(
												id = 2002L,
												nickname = "contributorTwo",
												profileImageUrl = "https://example.com/profile_contributortwo.jpg",
											),
										),
								),
						),
						UnsolvedGroupBookQuizSummary(
							book =
								UnsolvedGroupBookQuizSummary.Book(
									id = 2L,
									title = "Mystery of the Lost City",
									imageUrl = "https://example.com/mystery_lost_city.jpg",
								),
							quiz =
								UnsolvedGroupBookQuizSummary.Quiz(
									id = 102L,
									title = "Mystery Quiz",
									description = "description",
									creator =
										UnsolvedGroupBookQuizSummary.Creator(
											id = 1002L,
											nickname = "mysterySolver",
											profileImageUrl = "https://example.com/profile_mysterysolver.jpg",
										),
									createdAt = LocalDateTime.of(2024, 6, 15, 10, 0, 0, 0),
									contributors =
										listOf(
											UnsolvedGroupBookQuizSummary.Contributor(
												id = 2003L,
												nickname = "mysteryFan",
												profileImageUrl = "https://example.com/profile_mysteryfan.jpg",
											),
										),
								),
						),
					),
				)

			val params = pageRequestParams<UnsolvedGroupBookQuizSortKeyword>()

			performGet(Path("/book-quizzes/study-groups/{studyGroupId}/unsolved", "1"), params)
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/get-unsolved-study-group-quiz",
						pathParameters(parameterWithName("studyGroupId").description("스터디 그룹 ID")),
						queryParameters(
							*pageQueryParameters<UnsolvedGroupBookQuizSortKeyword>(),
						),
						responseFields(
							endPageNumberFields(),
							// Top level field
							fieldWithPath("data[].book").description("퀴즈에 관련된 책 정보"),
							fieldWithPath("data[].quiz").description("퀴즈의 상세 정보"),
							// Book summary fields
							fieldWithPath("data[].book.id").description("책의 고유 ID"),
							fieldWithPath("data[].book.title").description("책의 제목"),
							fieldWithPath("data[].book.imageUrl").description("책의 이미지 URL"),
							// Quiz summary fields
							fieldWithPath("data[].quiz.id").description("퀴즈의 고유 ID"),
							fieldWithPath("data[].quiz.title").description("퀴즈 제목"),
							fieldWithPath("data[].quiz.description").description("퀴즈 설명"),
							fieldWithPath("data[].quiz.creator").description("퀴즈 생성자 정보"),
							fieldWithPath("data[].quiz.createdAt").description("퀴즈 생성 날짜 및 시간"),
							fieldWithPath("data[].quiz.contributors").description("퀴즈 기여자 목록"),
							// Quiz creator fields
							fieldWithPath("data[].quiz.creator.id").description("퀴즈 생성자의 고유 ID"),
							fieldWithPath("data[].quiz.creator.nickname").description("퀴즈 생성자의 닉네임"),
							fieldWithPath("data[].quiz.creator.profileImageUrl")
								.description("퀴즈 생성자의 프로필 이미지 URL (선택 사항)"),
							// Quiz contributor fields (each contributor)
							fieldWithPath("data[].quiz.contributors[].id").description("퀴즈 기여자의 고유 ID"),
							fieldWithPath("data[].quiz.contributors[].nickname").description("퀴즈 기여자의 닉네임"),
							fieldWithPath("data[].quiz.contributors[].profileImageUrl")
								.description("퀴즈 기여자의 프로필 이미지 URL (선택 사항)"),
						),
					),
				)
		}

		"내가 작성한 퀴즈 목록을 조회한다" {
			every { findMyBookQuizUseCase.findMyBookQuiz(any(), any(), any()) } returns
				PageResponse.of(
					1000,
					20,
					listOf(
						MyBookQuizSummary(
							id = 1L,
							bookImageUrl = "https://example.com/book_image1.jpg",
							title = "Effective Kotlin",
							description = "Description",
							updatedAt = LocalDateTime.now().minusDays(2),
							studyGroup =
								MyBookQuizSummary.StudyGroup(
									1L,
									"Study Group 1",
									profileImageUrl = "hello.png",
								),
							temporary = false,
						),
						MyBookQuizSummary(
							id = 2L,
							bookImageUrl = "https://example.com/book_image2.jpg",
							title = "Kotlin in Action",
							description = "Description",
							updatedAt = LocalDateTime.now().minusDays(5),
							temporary = false,
						),
					),
				)
			val params =
				pageRequestParams<MyBookQuizSummarySortKeyword>(
					etc =
						mapOf(
							"temporary" to "false",
							"viewScope" to "EVERYONE",
							"studyGroup.only" to "false",
							"studyGroup.exclude" to "false",
							"studyGroup.id" to "1",
						),
				)
			performGet(Path("/book-quizzes/my"), params)
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/get-my-quiz",
						queryParameters(
							*pageQueryParameters<MyBookQuizSummarySortKeyword>(),
							parameterWithName("temporary")
								.description("임시 저장 여부 (default false)"),
							parameterWithName("viewScope")
								.description("보기 접근 권한 (optional) [${AccessScope.entries.joinToString(", ") { it.name }}]"),
							parameterWithName("studyGroup.only").description("스터디 그룹 퀴즈만 조회 (default false)"),
							parameterWithName("studyGroup.exclude").description("스터디 그룹 퀴즈 제외하고 조회 (default false)"),
							parameterWithName("studyGroup.id").description("스터디 그룹 ID (optional)"),
						),
						responseFields(
							endPageNumberFields(),
							fieldWithPath("data[].id").description("퀴즈 요약의 고유 ID"),
							fieldWithPath("data[].bookImageUrl").optional().description("책의 이미지 URL (optional)"),
							fieldWithPath("data[].title").description("퀴즈 타이틀"),
							fieldWithPath("data[].description").description("퀴즈 설명"),
							fieldWithPath("data[].updatedAt").description("마지막으로 업데이트된 시간 (ISO 8601 형식)"),
							fieldWithPath("data[].temporary").description("임시저장 여부"),
							fieldWithPath("data[].studyGroup").optional().description("스터디 그룹 정보 (optional)"),
							fieldWithPath("data[].studyGroup.id").description("스터디 그룹의 고유 식별자(ID)."),
							fieldWithPath("data[].studyGroup.name").description("스터디 그룹의 이름."),
							fieldWithPath("data[].studyGroup.profileImageUrl")
								.optional()
								.description("스터디 그룹의 프로필 이미지 URL. (optional)"),
						),
					),
				)
		}

		"퀴즈를 삭제한다" {
			every { deleteBookQuizUseCase.deleteBy(any(), any()) } returns Unit

			performDelete(Path("/book-quizzes/{id}", "1"))
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"book-quiz/delete",
						pathParameters(
							parameterWithName("id").description("book quiz id"),
						),
					),
				)
		}

		"퀴즈 설명을 조회한다" {
			every { bookQuizExplanationUseCase.findExplanationBy(any()) } returns
				BookQuizExplanation(
					id = 1L,
					title = "Sample Quiz Explanation",
					description = "This is a detailed explanation for a sample book quiz.",
					createdAt = LocalDateTime.now(),
					creator =
						BookQuizExplanation.Creator(
							id = 101L,
							nickname = "QuizMaster",
							profileImageUrl = "https://example.com/profile.jpg",
						),
					book =
						BookQuizExplanation.Book(
							id = 201L,
							title = "Effective Kotlin",
							imageUrl = "https://example.com/book.jpg",
						),
				)

			performGet(Path("/book-quizzes/{id}/explanation", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/explanation",
						pathParameters(
							parameterWithName("id").description("book quiz id"),
						),
						responseFields(
							fieldWithPath("id").description("퀴즈 설명의 고유 ID"),
							fieldWithPath("title").description("퀴즈 설명의 제목"),
							fieldWithPath("description").description("퀴즈 설명의 내용"),
							fieldWithPath("createdAt").description("퀴즈 설명이 생성된 날짜 및 시간 (ISO 8601 형식)"),
							fieldWithPath("creator").description("퀴즈 설명을 생성한 사용자의 정보"),
							fieldWithPath("creator.id").description("생성자의 고유 ID"),
							fieldWithPath("creator.nickname").description("생성자의 닉네임"),
							fieldWithPath("creator.profileImageUrl")
								.description("생성자의 프로필 이미지 URL (optional)")
								.optional(),
							fieldWithPath("book").description("퀴즈가 참조하는 책의 정보"),
							fieldWithPath("book.id").description("책의 고유 ID"),
							fieldWithPath("book.title").description("책의 제목"),
							fieldWithPath("book.imageUrl").description("책의 이미지 URL (optional)").optional(),
						),
					),
				)
		}

		"북 퀴즈를 상세 조회한다" {
			every { findBookQuizDetailUseCase.findBookQuizDetailBy(any()) } returns
				BookQuizDetail(
					id = 1L,
					title = "Introduction to Kotlin",
					description = "A quiz based on the Kotlin programming book.",
					bookId = 101L,
					questions =
						listOf(
							BookQuizDetail.Question(
								id = 1L,
								content = "What is a data class in Kotlin?",
								selectOptions =
									listOf(
										"A regular class",
										"A class with extra features",
										"A class that provides getters and setters",
									),
								answerExplanationContent = "A data class in Kotlin is a special class primarily used to hold data.",
								answerExplanationImages = listOf("image1.png", "image2.png"),
								answerType = QuizType.MULTIPLE_CHOICE_MULTIPLE_ANSWER,
								answers = listOf("A class with extra features"),
							),
							BookQuizDetail.Question(
								id = 2L,
								content = "Which keyword is used to declare a variable in Kotlin?",
								selectOptions = listOf("val", "var", "let"),
								answerExplanationContent = "In Kotlin",
								answerExplanationImages = listOf(),
								answerType = QuizType.MULTIPLE_CHOICE_SINGLE_ANSWER,
								answers = listOf("val", "var"),
							),
						),
					studyGroupId = 201L,
					timeLimitSecond = 3600,
					viewScope = AccessScope.CREATOR,
					editScope = AccessScope.CREATOR,
					temporary = true,
				)

			performGet(Path("/book-quizzes/{id}", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/detail",
						pathParameters(
							parameterWithName("id").description("book quiz id"),
						),
						responseFields(
							fieldWithPath("id").description("퀴즈의 고유 식별자."),
							fieldWithPath("title").description("퀴즈의 제목."),
							fieldWithPath("description").description("퀴즈에 대한 상세 설명."),
							fieldWithPath("bookId").description("퀴즈와 연결된 책의 ID."),
							fieldWithPath("questions").description("퀴즈에 포함된 질문 목록."),
							fieldWithPath("questions[].id").description("질문의 고유 식별자."),
							fieldWithPath("questions[].content").description("질문의 내용."),
							fieldWithPath("questions[].selectOptions").description("질문의 선택 가능한 옵션들."),
							fieldWithPath("questions[].answerExplanationContent").description("정답에 대한 설명."),
							fieldWithPath("questions[].answerExplanationImages").description("정답 설명에 사용되는 이미지 URL 목록."),
							fieldWithPath("questions[].answerType").description("질문의 유형"),
							fieldWithPath("questions[].answers").description("질문의 정답 목록."),
							fieldWithPath("studyGroupId").description("퀴즈와 연결된 스터디 그룹의 ID. (optional)"),
							fieldWithPath("timeLimitSecond").description("퀴즈의 제한 시간(초 단위). (optional)"),
							fieldWithPath("viewScope").description("퀴즈의 조회 접근 범위."),
							fieldWithPath("editScope").description("퀴즈의 수정 접근 범위."),
							fieldWithPath("temporary")
								.type(JsonFieldType.BOOLEAN)
								.description("임시 저장 여부"),
						),
					),
				)
		}
	}
}