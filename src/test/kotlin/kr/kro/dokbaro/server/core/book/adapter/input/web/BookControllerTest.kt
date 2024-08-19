package kr.kro.dokbaro.server.core.book.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.book.application.port.input.query.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.query.FindOneBookUseCase
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookAuthor
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
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

	init {
		"책 전체 조회를 수행한다" {
			every { findAllBookUseCase.findAllBy(any()) } returns
				listOf(
					Book(
						"1234567891234",
						"점프투자바",
						"위키북스",
						LocalDate.of(2024, 12, 11),
						10000,
						"이책 진짜 좋아요",
						"image.png",
						setOf(
							BookCategory(3, "IT", listOf()),
							BookCategory(5, "개발방법론", listOf()),
						),
						listOf(
							BookAuthor("마틴 파울러"),
							BookAuthor("조영호"),
						),
						1,
					),
					Book(
						"9865467891239",
						"title",
						"개발은 이렇게 해라",
						LocalDate.of(2024, 12, 11),
						10000,
						"이책 진짜 진짜 좋아요",
						"image.png",
						setOf(
							BookCategory(3, "IT", listOf()),
						),
						listOf(
							BookAuthor("박현준"),
						),
						1,
					),
				)

			val param =
				mapOf(
					"title" to "이펙티브 자바",
					"authorName" to "김우근",
					"description" to "책 설명",
					"category" to "4",
					"page" to "3",
					"limit" to "10",
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
							parameterWithName("limit").description("노출 개수"),
						),
						responseFields(
							fieldWithPath("[].isbn").type(JsonFieldType.STRING).description("isbn"),
							fieldWithPath("[].title").type(JsonFieldType.STRING).description("책 제목"),
							fieldWithPath("[].publisher").type(JsonFieldType.STRING).description("출판사"),
							fieldWithPath("[].publishedAt").type(JsonFieldType.STRING).description("출판일"),
							fieldWithPath("[].imageUrl").type(JsonFieldType.STRING).description("image url"),
							fieldWithPath("[].categories[].id").type(JsonFieldType.NUMBER).description("카테고리 id"),
							fieldWithPath("[].categories[].name").type(JsonFieldType.STRING).description("카테고리 이름"),
							fieldWithPath("[].authors").type(JsonFieldType.ARRAY).description("저자명"),
							fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("id"),
						),
					),
				)
		}

		"ID를 통한 책 조회를 수행한다" {
			every { findOneBookUseCase.findById(any()) } returns
				Book(
					"1234567891234",
					"점프투자바",
					"위키북스",
					LocalDate.of(2024, 12, 11),
					10000,
					"이책 진짜 좋아요",
					"image.png",
					setOf(
						BookCategory(3, "IT", listOf()),
						BookCategory(5, "개발방법론", listOf()),
					),
					listOf(
						BookAuthor("마틴 파울러"),
						BookAuthor("조영호"),
					),
					1,
				)

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
							fieldWithPath("publishedAt").type(JsonFieldType.STRING).description("출판일"),
							fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
							fieldWithPath("description").type(JsonFieldType.STRING).description("책 설명"),
							fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("image url"),
							fieldWithPath("categories[].id").type(JsonFieldType.NUMBER).description("카테고리 id"),
							fieldWithPath("categories[].name").type(JsonFieldType.STRING).description("카테고리 이름"),
							fieldWithPath("authors").type(JsonFieldType.ARRAY).description("저자명"),
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("id"),
						),
					),
				)
		}
	}
}