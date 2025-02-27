package kr.kro.dokbaro.server.core.studygroup.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.AccountType
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq.StudyGroupMapper
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupQueryRepository
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
import kr.kro.dokbaro.server.core.studygroup.application.port.out.dto.FindStudyGroupCondition
import kr.kro.dokbaro.server.core.studygroup.domain.InviteCode
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMember
import kr.kro.dokbaro.server.core.studygroup.domain.StudyMemberRole
import kr.kro.dokbaro.server.fixture.domain.studyGroupFixture
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
		val queryRepository = StudyGroupQueryRepository(dslContext, StudyGroupMapper())
		val adapter = StudyGroupPersistenceAdapter(repository)

		"study group 저장을 수행한다." {
			val savedMember =
				memberRepository.insert(
					Member(
						nickname = "nick",
						email = Email("www@gmail.com"),
						profileImage = "aaa.png",
						certificationId = UUID.randomUUID(),
						accountType = AccountType.SOCIAL,
					),
				)
			val studyGroup =
				studyGroupFixture(studyMembers = mutableSetOf(StudyMember(savedMember.id, StudyMemberRole.LEADER)))

			val id = adapter.insert(studyGroup)

			id shouldNotBe null
			studyGroupDao.findById(id) shouldNotBe null
			studyGroupMemberDao.findAll().count { it.studyGroupId == id } shouldBe 1
		}

		"update를 수행한다" {
			val savedMember =
				memberRepository.insert(
					Member(
						nickname = "nick",
						email = Email("www@gmail.com"),
						profileImage = "aaa.png",
						certificationId = UUID.randomUUID(),
						accountType = AccountType.SOCIAL,
					),
				)
			val studyGroup =
				studyGroupFixture(studyMembers = mutableSetOf(StudyMember(savedMember.id, StudyMemberRole.LEADER)))

			val id = adapter.insert(studyGroup)

			val savedMember2 =
				memberRepository.insert(
					Member(
						nickname = "nick2",
						email = Email("www2@gmail.com"),
						profileImage = "aaa.png",
						certificationId = UUID.randomUUID(),
						accountType = AccountType.SOCIAL,
					),
				)
			val newName = "new name"
			val newIntro = "new intro"
			val newCode = "NEW234"
			val newMembers =
				mutableSetOf(
					StudyMember(savedMember.id, StudyMemberRole.LEADER),
					StudyMember(savedMember2.id, StudyMemberRole.LEADER),
				)

			val newGroup =
				studyGroupFixture(
					id,
					newName,
					newIntro,
					null,
					newMembers,
					InviteCode(newCode),
				)

			adapter.update(newGroup)

			val result = queryRepository.findBy(FindStudyGroupCondition(inviteCode = newCode))!!

			result.name shouldBe newName
			result.introduction shouldBe newIntro
			result.inviteCode.value shouldBe newCode
			result.studyMembers shouldBe newMembers

			val updated2 =
				studyGroupFixture(
					id,
					newName,
					null,
					null,
					newMembers,
					InviteCode(newCode),
				)
			adapter.update(updated2)

			val result2 = queryRepository.findBy(FindStudyGroupCondition(inviteCode = newCode))!!
			result2.introduction shouldBe null
		}

		"삭제를 진행한다" {
			val savedMember =
				memberRepository.insert(
					Member(
						nickname = "nick",
						email = Email("www@gmail.com"),
						profileImage = "aaa.png",
						certificationId = UUID.randomUUID(),
						accountType = AccountType.SOCIAL,
					),
				)
			val studyGroup =
				studyGroupFixture(studyMembers = mutableSetOf(StudyMember(savedMember.id, StudyMemberRole.LEADER)))

			repository.insert(studyGroup)

			val savedStudyGroup =
				queryRepository.findBy(FindStudyGroupCondition(inviteCode = studyGroup.inviteCode.value))!!

			adapter.deleteStudyGroup(savedStudyGroup.id)

			queryRepository.findBy(FindStudyGroupCondition(inviteCode = studyGroup.inviteCode.value)) shouldBe null
		}
	})