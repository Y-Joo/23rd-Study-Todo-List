package com.example.studytodolist.todo.controller

import com.example.studytodolist.todo.dto.request.BulkSaveRequestDto
import com.example.studytodolist.todo.dto.request.TodoSaveRequestDto
import com.example.studytodolist.todo.dto.request.TodoUpdateRequestDto
import com.example.studytodolist.todo.dto.response.BulkSaveResponseDto
import com.example.studytodolist.todo.dto.response.TodoFindResponseDto
import com.example.studytodolist.todo.dto.response.TodoSaveResponseDto
import com.example.studytodolist.todo.dto.response.TodoUpdateResponseDto
import com.example.studytodolist.todo.service.TodoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.add
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@Tag(name = "Todo v1.0 API", description = "TodoList 관련 api")
@RestController
@RequestMapping(value = ["/api/v1/todo"], produces = [MediaType.APPLICATION_JSON_VALUE])
class TodoController(private val todoService: TodoService) {
    @Operation(
        summary = "Todo 생성",
        description = "Todo 정보를 받아 생성한다."
    )
    @PostMapping("")
    fun save(@RequestBody saveRequest: TodoSaveRequestDto): ResponseEntity<EntityModel<TodoSaveResponseDto>> {
        val todoSaveResponseDto = todoService.save(saveRequest)
        val entityModel = EntityModel.of(todoSaveResponseDto)
        entityModel.add(Link.of("/api/v1/todo/", "detail"))
        entityModel.add(Link.of("/api/v1/todo/list", "list"))
        return ResponseEntity.created(URI.create("/api/v1/todo/"))
            .body(entityModel)
    }

    @Operation(
        summary = "Todo 리스트 조회",
        description = "모든 Todo를 반환한다."
    )
    @GetMapping("")
    fun findAll(): ResponseEntity<EntityModel<List<TodoFindResponseDto>>> {
        val todoFindResponseDto = todoService.findAll()
        val entityModel = EntityModel.of(todoFindResponseDto)
        entityModel.add(Link.of("/api/v1/todo", "detail"))
        entityModel.add(Link.of("/api/v1/todo", "update"))
        entityModel.add(Link.of("/api/v1/todo", "delete"))
        return ResponseEntity.ok(entityModel)
    }

    @Operation(
        summary = "id로 Todo 조회",
        description = "id를 받아 해당하는 Todo를 반환한다."
    )
    @GetMapping("")
    fun findById(@RequestParam id: Long): ResponseEntity<EntityModel<TodoFindResponseDto>> {
        val todoFindResponseDto = todoService.findById(id)
        val entityModel = EntityModel.of(todoFindResponseDto)
        entityModel.add(Link.of("/api/v1/todo", "update"))
        entityModel.add(Link.of("/api/v1/todo", "delete"))
        entityModel.add(Link.of("/api/v1/todo/list", "list"))
        return ResponseEntity.ok(entityModel)
    }

    @Operation(
        summary = "Todo 삭제",
        description = "id를 받아 해당하는 Todo를 삭제한다."
    )
    @DeleteMapping("")
    fun delete(@RequestParam id: Long): ResponseEntity<Void> {
        todoService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Todo progress 업데이트",
        description = "id와 progress를 받아 해당하는 Todo의 progress를 업데이트한다."
    )
    @PatchMapping("")
    fun update(@RequestBody updateRequest: TodoUpdateRequestDto): ResponseEntity<EntityModel<TodoUpdateResponseDto>> {
        val todoUpdateResponseDto = todoService.update(updateRequest)
        val entityModel = EntityModel.of(todoUpdateResponseDto)
        entityModel.add(Link.of("/api/v1/todo", "detail"))
        entityModel.add(Link.of("/api/v1/todo/list", "list"))
        return ResponseEntity.ok(entityModel)
    }

    @Operation(
        summary = "Todo bulk 생성",
        description = "count를 받아 해당하는 개수의 mock data를 bulk save 한 후, 생성한 개수를 반환한다.")
    @PostMapping("/bulk")
    fun bulkSave(@RequestBody bulkSaveRequestDto: BulkSaveRequestDto): ResponseEntity<BulkSaveResponseDto>{
        val bulkSaveResponseDto = todoService.bulkSave(bulkSaveRequestDto)
        val entityModel = EntityModel.of(bulkSaveResponseDto)
        entityModel.add(Link.of("/api/v1/todo/", "detail"))
        entityModel.add(Link.of("/api/v1/todo/list", "list"))
        return ResponseEntity.created(URI.create("/api/v1/todo")).body(todoService.bulkSave(bulkSaveRequestDto))
    }
}