package com.filmsaboinf.ofnimlif.repository

import com.filmsaboinf.ofnimlif.logcat
import com.filmsaboinf.ofnimlif.repository.model.FilmPreview
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DefaultRepository {

    suspend fun getFilmsFromPage(page: Int): List<FilmPreview> {
        val listTitles = mutableListOf<String>()
        val listImages = mutableListOf<String>()
        val listUrl = mutableListOf<String>()

        val listOfFilms = mutableListOf<FilmPreview>()

        val doc: Document =
            Jsoup.connect("https://moviesjoy.to/filter?type=all&quality=all&release_year=all&genre=9&country=all&page=$page")
                .get()

        doc.select("h2.film-name").select("a").also { elements ->
            for (i in elements) {
                listTitles.add(i.text())
            }
        }

        doc.select("img.film-poster-img").also { elements ->
            for (i in elements) {
                listImages.add(i.attr("data-src"))
            }
        }

        doc.select("div.film-poster").select("a").also { elements ->
            for (i in elements) {
                listUrl.add("https://moviesjoy.to" + i.attr("href"))
            }
        }

        try {
            for (i in listImages.indices) {
                listOfFilms.add(
                    FilmPreview(
                        name = listTitles[i],
                        img = listImages[i],
                        urlDetail = listUrl[i]
                    )
                )
            }
        } catch (e: java.lang.Exception) {
            logcat("error inflate list film(Repository): $e")
        }


        return listOfFilms
    }

    suspend fun loadFilmPage(film: FilmPreview): FilmPreview {

        val doc: Document =
            Jsoup.connect(film.urlDetail).get()

        film.description = "N/A"
        film.rate = "N/A"
        film.country = "N/A"

        doc.let {
            film.description = it.select("div.description").first()?.text() ?: "none"
            film.rate = it.select("button.btn-imdb").text().replace("IMDB: ", "")
            it.select("div.row-line").select("a").also { el ->
                for (i in el) {
                    if (i.attr("href").contains("/country/")) {
                        film.country = i.text()
                    }
                }


            }

        }

        return film
    }

}