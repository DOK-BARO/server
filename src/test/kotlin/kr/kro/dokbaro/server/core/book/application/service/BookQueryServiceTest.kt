package kr.kro.dokbaro.server.core.book.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.application.port.out.CountBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookPort
import kr.kro.dokbaro.server.core.book.application.port.out.ReadIntegratedBookCollectionPort
import kr.kro.dokbaro.server.core.book.application.service.exception.BookNotFoundException
import kr.kro.dokbaro.server.core.book.query.BookSummarySortOption
import kr.kro.dokbaro.server.fixture.domain.bookDetailFixture
import kr.kro.dokbaro.server.fixture.domain.bookSummaryFixture
import kr.kro.dokbaro.server.fixture.domain.integratedBookFixture

class BookQueryServiceTest :
	StringSpec({
		val readBookCollectionPort = mockk<ReadBookCollectionPort>()
		val readBookPort = mockk<ReadBookPort>()
		val readIntegratedBookCollectionPort = mockk<ReadIntegratedBookCollectionPort>()
		val countBookPort = mockk<CountBookPort>()

		val bookQueryService =
			BookQueryService(readBookCollectionPort, readBookPort, readIntegratedBookCollectionPort, countBookPort)

		afterEach {
			clearAllMocks()
		}

		"전체 조회를 수행한다" {
			every { countBookPort.countBy(any()) } returns 100
			every { readBookCollectionPort.getAllBook(any(), any(), any()) } returns
				listOf(
					bookSummaryFixture(),
					bookSummaryFixture(),
					bookSummaryFixture(),
				)

			bookQueryService
				.findAllBy(
					FindAllBookCommand(
						page = 1,
						size = 10,
						sort = BookSummarySortOption.TITLE,
						direction = SortDirection.ASC,
					),
				).data.size shouldBe
				3
		}

		"단일 조회를 수행한다" {
			every { readBookPort.findBy(5) } returns
				bookDetailFixture(id = 5)

			bookQueryService.getBy(5) shouldNotBe null
		}

		"단일 조회 시 ID에 해당하는 책이 없으면 예외를 반환한다" {
			every { readBookPort.findBy(any()) } returns null

			shouldThrow<BookNotFoundException> {
				bookQueryService.getBy(5)
			}
		}

		"통합 검색을 수행한다" {
			every { readIntegratedBookCollectionPort.findAllIntegratedBook(any(), any(), any()) } returns
				listOf(
					integratedBookFixture(),
					integratedBookFixture(),
					integratedBookFixture(),
				)

			bookQueryService.findAllIntegratedBooks(10, "asdf", null).size shouldBe 3
		}
	})