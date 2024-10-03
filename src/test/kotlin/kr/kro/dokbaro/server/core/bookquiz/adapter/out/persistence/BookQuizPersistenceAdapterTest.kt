package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.book.adapter.out.persistence.repository.jooq.BookRepository
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq.StudyGroupMapper
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.fixture.domain.bookFixture
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture
import org.jooq.DSLContext

@PersistenceAdapterTest
class BookQuizPersistenceAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val bookRepository = BookRepository(dslContext)
		val bookQuizRepository = BookQuizRepository(dslContext)
		val studyGroupRepository = StudyGroupRepository(dslContext, StudyGroupMapper())
		val adapter = BookQuizPersistenceAdapter(bookQuizRepository)

		"신규 book quiz를 생성한다" {
			val memberId = memberRepository.insert(memberFixture()).id
			val bookId = bookRepository.insertBook(bookFixture())
			val studyGroupId =
				studyGroupRepository.insert(
					studyGroupFixture(
						studyMembers = mutableSetOf(StudyMember(memberId, StudyMemberRole.LEADER)),
					),
				)
			adapter.insert(
				bookQuizFixture(bookId = bookId, creatorId = memberId, studyGroups = listOf(studyGroupId)),
			) shouldNotBe
				null
		}
	})