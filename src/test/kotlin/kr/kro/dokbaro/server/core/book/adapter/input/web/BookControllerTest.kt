package kr.kro.dokbaro.server.core.book.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindIntegratedBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindOneBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCommand
import kr.kro.dokbaro.server.fixture.domain.bookDetailFixture
import kr.kro.dokbaro.server.fixture.domain.bookSummaryFixture
import kr.kro.dokbaro.server.fixture.domain.integratedBookFixture
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@WebMvcTest(BookController::class)
class BookControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var findAllBookUseCase: FindAllBookUseCase

	@MockkBean
	lateinit var findOneBookUseCase: FindOneBookUseCase

	@MockkBean
	lateinit var createBookUseCase: CreateBookUseCase

	@MockkBean
	lateinit var findIntegratedBookUseCase: FindIntegratedBookUseCase

	init {
		"책 전체 조회를 수행한다" {
			every { findAllBookUseCase.findAllBy(any(), any()) } returns
				PageResponse(
					100,
					listOf(
						bookSummaryFixture(),
						bookSummaryFixture(title = "제목"),
					),
				)

			val param =
				mapOf(
					"title" to "이펙티브 자바",
					"authorName" to "김우근",
					"description" to "책 설명",
					"category" to "4",
					"page" to "3",
					"size" to "10",
					"sort" to "TITLE",
				)
			performGet(Path("/books"), param)
				.andExpect(status().isOk)
				.andDo(
					print(
						"book/find-book-collection",
						queryParameters(
							parameterWithName("title").description("책 제목 (optional)").optional(),
							parameterWithName("authorName").description("저자 명 (optional)").optional(),
							parameterWithName("description").description("책 설명 (optional)").optional(),
							parameterWithName("category").description("카테고리 ID (optional)").optional(),
							parameterWithName("page").description("page 번호. 1부터 시작. (default : 1)").optional(),
							parameterWithName("size").description("노출 개수"),
							parameterWithName("sort")
								.description("정렬 기준. ( PUBLISHED_AT, TITLE, QUIZ_COUNT )"),
							parameterWithName("direction")
								.description("정렬 방향. ( 'ASC' , 'DESC' ) default: ASC")
								.optional(),
						),
						responseFields(
							fieldWithPath("endPageNumber")
								.type(JsonFieldType.NUMBER)
								.description("마지막 페이지 번호."),
							fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("id"),
							fieldWithPath("data[].title").type(JsonFieldType.STRING).description("책 제목"),
							fieldWithPath("data[].publisher").type(JsonFieldType.STRING).description("출판사"),
							fieldWithPath("data[].imageUrl").type(JsonFieldType.STRING).description("image url"),
							fieldWithPath("data[].authors").type(JsonFieldType.ARRAY).description("저자명"),
							fieldWithPath("data[].quizCount").type(JsonFieldType.NUMBER).description("관련 quiz 개수"),
						),
					),
				)
		}

		"ID를 통한 책 조회를 수행한다" {
			every { findOneBookUseCase.getBy(any()) } returns bookDetailFixture()

			performGet(Path("/books/{id}", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"book/find-book",
						pathParameters(parameterWithName("id").description("도서 ID")),
						responseFields(
							fieldWithPath("isbn").type(JsonFieldType.STRING).description("isbn"),
							fieldWithPath("title").type(JsonFieldType.STRING).description("책 제목"),
							fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사"),
							fieldWithPath("description").type(JsonFieldType.STRING).description("책 설명"),
							fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("image url"),
							fieldWithPath("categories[].id").type(JsonFieldType.NUMBER).description("카테고리 id"),
							fieldWithPath("categories[].name").type(JsonFieldType.STRING).description("카테고리 이름"),
							subsectionWithPath("categories[].parent").type(JsonFieldType.OBJECT).description("상위 카테고리"),
							fieldWithPath("authors").type(JsonFieldType.ARRAY).description("저자명"),
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
						),
					),
				)
		}

		"책 생성을 수행한다" {
			every { createBookUseCase.create(any(), any()) } returns 1

			val body =
				CreateBookCommand(
					isbn = "9783161484100",
					title = "Effective Kotlin",
					publisher = "TechBooks",
					publishedAt = LocalDate.now(),
					price = 45000,
					description = "A comprehensive guide to writing high-quality Kotlin code.",
					imageUrl = "https://example.com/images/effective-kotlin.jpg",
					categories = setOf(1L, 2L, 3L),
					authors = listOf("Marcin Moskala", "Igor Wojda"),
				)

			performPost(Path("/books"), body)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"book/create-book",
						requestFields(
							fieldWithPath("isbn")
								.type(JsonFieldType.STRING)
								.description("isbn"),
							fieldWithPath("title").type(JsonFieldType.STRING).description("책 제목"),
							fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사"),
							fieldWithPath("publishedAt")
								.type(JsonFieldType.STRING)
								.description("출판일 (YYYY-MM-DD)"),
							fieldWithPath("price")
								.type(JsonFieldType.NUMBER)
								.description("가격"),
							fieldWithPath("description")
								.type(JsonFieldType.STRING)
								.optional()
								.description("책 설명 (optional)"),
							fieldWithPath("imageUrl")
								.type(JsonFieldType.STRING)
								.optional()
								.description("책 이미지 URL (optional)"),
							fieldWithPath("categories")
								.type(JsonFieldType.ARRAY)
								.description("책 category id 들"),
							fieldWithPath("authors")
								.type(JsonFieldType.ARRAY)
								.description("저자"),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("저장된 책의 id"),
						),
					),
				)
		}

		"통합 검색을 수행한다" {
			every { findIntegratedBookUseCase.findAllIntegratedBooks(any(), any(), any()) } returns
				listOf(
					integratedBookFixture(),
					integratedBookFixture(title = "제목"),
				)

			val param =
				mapOf(
					"keyword" to "자바",
					"lastId" to "3",
					"size" to "10",
				)
			performGet(Path("/books/integrated"), param)
				.andExpect(status().isOk)
				.andDo(
					print(
						"book/find-integrated-book-collection",
						queryParameters(
							parameterWithName("keyword").description("검색 키워드"),
							parameterWithName("lastId")
								.description("조회한 값 중 마지막 ID, 최초 조회 시 null (optional)")
								.optional(),
							parameterWithName("size").description("노출 개수"),
						),
						responseFields(
							fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("id"),
							fieldWithPath("[].title").type(JsonFieldType.STRING).description("책 제목"),
							fieldWithPath("[].publisher").type(JsonFieldType.STRING).description("출판사"),
							fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("image url"),
							fieldWithPath("[].authors").type(JsonFieldType.ARRAY).description("저자명"),
						),
					),
				)
		}
	}
}