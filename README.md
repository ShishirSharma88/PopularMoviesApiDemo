# Popular Movies

This repository contains classes for retrieving list of data related to movies from IMDb REST Api in date wise sorted way
It uses MVP design with RX Android, Retrofit 2, Picasso, ButterKnife, libraries. 
This Popular Movies application gives list of data with their title, images and content of a movie on click.

## Dependency

This app is developed with Android Studio 3.1.1 and latest Gradle version 3.1.1 Hence use -Android Studio 3.0 or Above version 
to edit or for testing at your own machine.
Compile SDK version 27 and Min SDK 23.

## Features

It shows product articles with their title, Description and Image in a list.
User is able to scroll and refresh as per need via swipe down on the screen to get the saved Data.
User can pullTorefresh to clear the fields like fromDate and toDate to get its normal api run.
It uses RxAndroid with MVP in its architecture.
It uses Retrofit 2 library for network calls.
Its uses Picasso for image download and cache management.
ButterKnife is used with views

## Folder Structure

This application's structure can be divided into 2 parts Business and UI.
Business layer contains - Data = contains core API and Usecase classes for retrofit 2 calls - model = contain the POJO classes DataObject 
and DomainModel as JSON responses
UI layer contains - Activity, MVP(presenter, contract interface and Recylcer view adapter)

## BUILD and RUN

Use Android Studio 3.1.1 and Gradle 3.1.1 to build the project.

## Drawback of api

The api gives data in unsorted way with respect to date, which changes order when we sort and show in pagination
