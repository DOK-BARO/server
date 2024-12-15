package kr.kro.dokbaro.server.core.book.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCommand
import kr.kro.dokbaro.server.core.book.application.port.out.InsertBookPort
import kr.kro.dokbaro.server.core.book.application.service.auth.BookAuthorityCheckService
import kr.kro.dokbaro.server.fixture.domain.dokbaroAdminFixture
import java.time.LocalDate

class BookServiceTest :
	StringSpec({
		val insertBookPort = mockk<InsertBookPort>()

		val service = BookService(insertBookPort, BookAuthorityCheckService())
		"신규 책 생성을 수행한다" {
			every { insertBookPort.insert(any()) } returns 1

			val command =
				CreateBookCommand(
					isbn = "9783161484100",
					title = "Effective Kotlin",
					publisher = "Manning Publications",
					publishedAt = LocalDate.of(2020, 5, 15),
					price = 35000,
					description = "A guide to Kotlin best practices.",
					imageUrl = "http://example.com/effective_kotlin.jpg",
					categories = setOf(1L, 2L),
					authors = listOf("Marcin Moskala"),
				)

			service.create(command, dokbaroAdminFixture()) shouldBe 1
		}
	})