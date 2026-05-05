package co.com.monkeymobile.fakestore.data.mapper

import co.com.monkeymobile.fakestore.data.local.entity.FavoriteEntity
import co.com.monkeymobile.fakestore.data.remote.dto.ProductDto
import co.com.monkeymobile.fakestore.data.remote.dto.UserDto
import co.com.monkeymobile.fakestore.domain.model.Name
import co.com.monkeymobile.fakestore.domain.model.Product
import co.com.monkeymobile.fakestore.domain.model.Rating
import co.com.monkeymobile.fakestore.domain.model.User

fun ProductDto.toDomain(isFavorite: Boolean = false) = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rating = Rating(rating.rate, rating.count),
    isFavorite = isFavorite
)

fun Product.toEntity() = FavoriteEntity(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rate = rating.rate,
    count = rating.count
)

fun FavoriteEntity.toDomain() = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rating = Rating(rate, count),
    isFavorite = true
)

fun UserDto.toDomain() = User(
    id = id,
    email = email,
    username = username,
    password = password,
    name = Name(name.firstname, name.lastname),
    phone = phone,
    v = v
)