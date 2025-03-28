package kr.kro.dokbaro.server.core.book.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.configuration.annotation.RestDocsTest
import kr.kro.dokbaro.server.configuration.docs.FieldType
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsExecutor
import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindIntegratedBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindOneBookUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCommand
import kr.kro.dokbaro.server.core.book.query.BookSummarySortKeyword
import kr.kro.dokbaro.server.fixture.adapter.input.web.pageQueryParameters
import kr.kro.dokbaro.server.fixture.domain.bookDetailFixture
import kr.kro.dokbaro.server.fixture.domain.bookSummaryFixture
import kr.kro.dokbaro.server.fixture.domain.integratedBookFixture
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@RestDocsTest
@WebMvcTest(BookController::class)
class BookControllerTest : StringSpec() {
	override fun extensions() = listOf(SpringExtension)

	@Autowired
	lateinit var mvc: MockMvc

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

			RestDocsExecutor(mvc, HttpMethod.GET, Path("/books")) {
				given {
					parameters {
						"title" to "이펙티브 자바"
						"authorName" to "김우근"
						"description" to "책 설명"
						"category" to "4"
					}
				}
				then {
					expect { status().isOk }
					docsTitle { "book/find-book-collection" }
					queryParameters {
						"title" means "책 제목" optional true
						"authorName" means "저자 명" optional true
						"description" means "책 설명" optional true
						"category" means "카테고리 ID" optional true
						pageQueryParameters<BookSummarySortKeyword>()
					}
					responseFields {
						"endPageNumber" type FieldType.NUMBER means "마지막 페이지 번호"
						"data[].id" type FieldType.NUMBER means "ID"
						"data[].title" type FieldType.STRING means "책 제목"
						"data[].publisher" type FieldType.STRING means "출판사"
						"data[].imageUrl" type FieldType.STRING means "image url"
						"data[].authors" type FieldType.ARRAY(FieldType.STRING) means "저자명"
						"data[].quizCount" type FieldType.NUMBER means "관련 quiz 개수"
					}
				}
			}
		}

		"ID를 통한 책 조회를 수행한다" {
			every { findOneBookUseCase.getBy(any()) } returns bookDetailFixture()

			RestDocsExecutor(mvc, HttpMethod.GET, Path("/books/{id}", "1")) {
				then {
					expect { status().isOk }
					docsTitle { "book/find-book" }
					pathParameters {
						"id" means "도서 ID"
					}
					responseFields {
						"id" type FieldType.NUMBER means "id"
						"isbn" type FieldType.STRING means "isbn"
						"title" type FieldType.STRING means "책 제목"
						"publisher" type FieldType.STRING means "출판사"
						"description" type FieldType.STRING means "책 설명"
						"imageUrl" type FieldType.STRING means "image url"
						"categories[].id" type FieldType.NUMBER means "카테고리 id"
						"categories[].name" type FieldType.STRING means "카테고리 이름"
						"categories[].parent" type FieldType.OBJECT means "상위 카테고리"
						"authors" type FieldType.ARRAY(FieldType.STRING) means "저자명"
					}
				}
			}
		}

		"책 생성을 수행한다" {
			every { createBookUseCase.create(any(), any()) } returns 1

			RestDocsExecutor(mvc, HttpMethod.POST, Path("/books")) {
				given {
					body {
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
					}
				}
				then {
					expect { status().isCreated }
					docsTitle { "book/create-book" }
					requestFields {
						"isbn" type FieldType.STRING means "isbn"
						"title" type FieldType.STRING means "책 제목"
						"publisher" type FieldType.STRING means "출판사"
						"publishedAt" type FieldType.DATE means "출판일"
						"price" type FieldType.NUMBER means "가격"
						"description" type FieldType.STRING means "책 설명" optional true
						"imageUrl" type FieldType.STRING means "책 이미지 URL" optional true
						"categories" type FieldType.ARRAY(FieldType.NUMBER) means "책 category id"
						"authors" type FieldType.ARRAY(FieldType.STRING) means "저자"
					}
					responseFields {
						"id" type FieldType.NUMBER means "저장된 책의 id"
					}
				}
			}
		}

		"통합 검색을 수행한다" {
			every { findIntegratedBookUseCase.findAllIntegratedBooks(any(), any(), any()) } returns
				listOf(
					integratedBookFixture(),
					integratedBookFixture(title = "제목"),
				)

			RestDocsExecutor(mvc, HttpMethod.GET, Path("/books/integrated")) {
				given {
					parameters {
						"keyword" to "자바"
						"lastId" to "3"
						"size" to "10"
					}
				}
				then {
					expect { status().isOk }
					docsTitle { "book/find-integrated-book-collection" }
					queryParameters {
						"keyword" means "검색 키워드"
						"lastId" means "조회한 값 중 마지막 ID, 최초 조회 시 null" optional true
						"size" means "노출 개수"
					}
					responseFields {
						"[].id" type FieldType.NUMBER means "id"
						"[].title" type FieldType.STRING means "책 제목"
						"[].publisher" type FieldType.STRING means "출판사"
						"[].imageUrl" type FieldType.STRING means "image url"
						"[].authors" type FieldType.ARRAY(FieldType.STRING) means "저자명"
					}
				}
			}
		}
	}
}