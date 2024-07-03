package kr.kro.dokbaro.server.global.template.port

interface SavePort<C, R> {
	fun save(command: C): R
}