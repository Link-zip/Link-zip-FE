package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.TestModel

data class TestResponse (
    val body: String
){
    fun toTestModel() = TestModel(body)
}