package kr.kro.dokbaro.server.core.book.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookCollectionPort
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookAuthor
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import java.time.LocalDate

class BookServiceTest :
	StringSpec({
		val loadBookCollectionPort = mockk<LoadBookCollectionPort>()

		val bookService = BookService(loadBookCollectionPort)

		"책 목록 조회를 수행한다" {
			val fixtures =
				listOf(
					Book(
						"1234567891234",
						"점프투자바",
						"위키북스",
						LocalDate.of(2024, 12, 11),
						10000,
						"이책 진짜 좋아요",
						"image.png",
						listOf(
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
						listOf(
							BookCategory(3, "IT", listOf()),
						),
						listOf(
							BookAuthor("박현준"),
						),
						1,
					),
				)
			every { loadBookCollectionPort.getAllBook(any(), any()) } returns fixtures

			val result = bookService.findBy(FindAllBookCommand(null, null, null, null, null, 5))

			result.size shouldBe fixtures.size
		}
	})