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
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.entity.jooq.StudyGroupMapper
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupRepository
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

		val repository = StudyGroupRepository(dslContext, StudyGroupMapper())
		val adapter = StudyGroupPersistenceAdapter(repository)

		"study group 저장을 수행한다." {
			val savedMember =
				memberRepository.insert(
					Member(
						nickName = "nick",
						email = Email("www@gmail.com"),
						profileImage = "aaa.png",
						certificationId = UUID.randomUUID(),
					),
				)
			val studyGroup =
				studyGroupFixture(studyMembers = mutableSetOf(StudyMember(savedMember.id, StudyMemberRole.LEADER)))

			val id = adapter.insert(studyGroup)

			id shouldNotBe null
			studyGroupDao.findById(id) shouldNotBe null
			studyGroupMemberDao.findAll().count { it.studyGroupId == id } shouldBe 1
		}

		"초대 코드를 통한 검색을 수행한다" {
			val savedMember =
				memberRepository.insert(
					Member(
						nickName = "nick",
						email = Email("www@gmail.com"),
						profileImage = "aaa.png",
						certificationId = UUID.randomUUID(),
					),
				)
			val targetCode = "ABC123"

			val studyGroup =
				studyGroupFixture(
					studyMembers = mutableSetOf(StudyMember(savedMember.id, StudyMemberRole.LEADER)),
					inviteCode = InviteCode(targetCode),
					introduction = null,
				)
			adapter.insert(studyGroup)

			adapter.findByInviteCode(targetCode) shouldNotBe null
			adapter.findByInviteCode("123ABC") shouldBe null
		}

		"update를 수행한다" {
			val savedMember =
				memberRepository.insert(
					Member(
						nickName = "nick",
						email = Email("www@gmail.com"),
						profileImage = "aaa.png",
						certificationId = UUID.randomUUID(),
					),
				)
			val studyGroup =
				studyGroupFixture(studyMembers = mutableSetOf(StudyMember(savedMember.id, StudyMemberRole.LEADER)))

			val id = adapter.insert(studyGroup)

			val savedMember2 =
				memberRepository.insert(
					Member(
						nickName = "nick2",
						email = Email("www2@gmail.com"),
						profileImage = "aaa.png",
						certificationId = UUID.randomUUID(),
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

			val result = adapter.findByInviteCode(newCode)!!

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

			val result2 = adapter.findByInviteCode(newCode)!!
			result2.introduction shouldBe null
		}
	})