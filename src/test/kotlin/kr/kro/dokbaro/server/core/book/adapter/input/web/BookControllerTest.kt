package kr.kro.dokbaro.server.core.book.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindOneBookUseCase
import kr.kro.dokbaro.server.core.book.query.BookCategorySingle
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookController::class)
class BookControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var findAllBookUseCase: FindAllBookUseCase

	@MockkBean
	lateinit var findOneBookUseCase: FindOneBookUseCase

	@MockkBean
	lateinit var createBookUseCase: CreateBookUseCase

	init {
		"책 전체 조회를 수행한다" {
			every { findAllBookUseCase.findAllBy(any()) } returns
				listOf(
					BookSummary(
						1,
						"점프투자바",
						"위키북스",
						"image.png",
						listOf(
							"마틴 파울러",
							"조영호",
						),
					),
					BookSummary(
						1,
						"개발은진짜이렇게해요",
						"위키북스",
						"image.png",
						listOf(
							"마틴 파울러",
							"조영호",
						),
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

		"ID를 통한 책 조회를 수행한다" {
			every { findOneBookUseCase.getBy(any()) } returns
				BookDetail(
					1,
					"1234567891234",
					"점프투자바",
					"위키북스",
					"이책 진짜 좋아요",
					"image.png",
					listOf(
						BookCategorySingle(
							7,
							"TCP",
							BookCategorySingle(
								4,
								"네트워크",
								BookCategorySingle(2, "IT", null),
							),
						),
						BookCategorySingle(5, "개발방법론", BookCategorySingle(2, "IT", null)),
					),
					listOf(
						"마틴 파울러",
						"조영호",
					),
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
	}
}