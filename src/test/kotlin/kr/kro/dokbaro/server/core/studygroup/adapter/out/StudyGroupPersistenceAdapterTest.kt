package kr.kro.dokbaro.server.core.studygroup.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.StudyGroupDao
import org.jooq.generated.tables.daos.StudyGroupMemberDao
import java.util.UUID

@PersistenceAdapterTest
class StudyGroupPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())

		val studyGroupDao = StudyGroupDao(configuration)
		val studyGroupMemberDao = StudyGroupMemberDao(configuration)

		val repository = StudyGroupRepository(dslContext)
		val adapter = StudyGroupPersistenceAdapter(repository)

		"study group 저장을 수행한다." {
			val savedMember =
				memberRepository.save(
					Member(
						"nick",
						Email("www@gmail.com"),
						"aaa.png",
						UUID.randomUUID(),
					),
				)
			val studyGroup =
				StudyGroup(
					"name",
					"introduction",
					"profileImage.png",
					savedMember.id,
				)

			val id = adapter.save(studyGroup)

			id shouldNotBe null
			studyGroupDao.findById(id) shouldNotBe null
			studyGroupMemberDao.findAll().count { it.studyGroupId == id } shouldBe 1
		}
	})