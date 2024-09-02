package kr.kro.dokbaro.server.core.emailauthentication.application.service

class CodeGeneratorStub : EmailCodeGenerator {
	override fun generate(): String = "ABCEDF"
}