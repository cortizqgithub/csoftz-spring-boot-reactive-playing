package com.csoftz.reactive.playing.controller.api;

import static org.springframework.hateoas.CollectionModel.of;
import static org.springframework.hateoas.mediatype.alps.Alps.alps;
import static org.springframework.hateoas.mediatype.alps.Alps.descriptor;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.alps.Alps;
import org.springframework.hateoas.mediatype.alps.Type;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csoftz.reactive.playing.domain.commerce.Item;
import com.csoftz.reactive.playing.repository.ItemRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hypermedia")
public class HypermediaItemController {

    private final ItemRepository repository;

    @GetMapping()
    public Mono<RepresentationModel<?>> root() {
        HypermediaItemController controller = methodOn(HypermediaItemController.class);

        return linkTo(controller.root())
            .withSelfRel()
            .toMono()
            .zipWith(linkTo(controller.findAll()).withRel(IanaLinkRelations.ITEM).toMono())
            .map(links -> Links.of(links.getT1(), links.getT2()))
            .map(links -> new RepresentationModel<>(links.toList()));
    }

    @GetMapping("/items")
    public Mono<CollectionModel<EntityModel<Item>>> findAll() {
        return this.repository
            .findAll()
            .flatMap(item -> findOne(item.getId()))
            .collectList()
            .flatMap(
                entityModels ->
                    linkTo(methodOn(HypermediaItemController.class).findAll())
                        .withSelfRel()
                        .toMono()
                        .map(selfLink -> of(entityModels, selfLink)));
    }

    @GetMapping("/items/{id}")
    public Mono<EntityModel<Item>> findOne(@PathVariable String id) {
        HypermediaItemController controller = methodOn(HypermediaItemController.class);

        return Mono.zip(
            repository.findById(id),
            linkTo(controller.findOne(id)).withSelfRel().toMono(),
            linkTo(controller.findAll()).withRel(IanaLinkRelations.ITEM).toMono())
            .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    @GetMapping("/items/{id}/affordances")
    public Mono<EntityModel<Item>> findOneWithAffordances(@PathVariable String id) {
        HypermediaItemController controller = methodOn(HypermediaItemController.class);

        return Mono.zip(
            repository.findById(id),
            linkTo(controller.findOne(id))
                .withSelfRel()
                .andAffordance(controller.updateItem(null, id))
                .toMono(),
            linkTo(controller.findAll()).withRel(IanaLinkRelations.ITEM).toMono())
            .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    @PostMapping("/items")
    public Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<EntityModel<Item>> item) {
        return item.map(EntityModel::getContent)
            .flatMap(this.repository::save)
            .map(Item::getId)
            .flatMap(this::findOne)
            .map(
                newModel ->
                    ResponseEntity.created(newModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                        .build());
    }

    @PutMapping("/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<EntityModel<Item>> item, @PathVariable String id) {
        return item.map(EntityModel::getContent)
            .map(
                content ->
                    new Item(id, content.getName(), content.getDescription(), content.getPrice()))
            .flatMap(this.repository::save)
            .then(findOne(id))
            .map(
                model ->
                    ResponseEntity.noContent()
                        .location(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                        .build());
    }

    @GetMapping(value = "/items/profile", produces = MediaTypes.ALPS_JSON_VALUE)
    public Alps profile() {
        return alps()
            .descriptor(
                Collections.singletonList(
                    descriptor()
                        .id(Item.class.getSimpleName() + "-repr")
                        .descriptor(
                            Arrays.stream(Item.class.getDeclaredFields())
                                .map(
                                    field ->
                                        descriptor().name(field.getName()).type(Type.SEMANTIC).build())
                                .collect(Collectors.toList()))
                        .build()))
            .build();
    }
}
