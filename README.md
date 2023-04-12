# KATA LegacyTrain

## Context

TrainTrain is a start-up aiming to help passengers to smoothly reserve seats on national trains via their Web API.
TrainTrain system is actually leveraging on 2 underlying Web Apis provided by the Hassan Cehef national train operator (
to get trains topologies and to confirm seats reservations once found, and to get official booking references for those
reservations).

A few months ago, a v1 of the TrainTrain.Api has been developed and released by an external consultancy but it seems
that they are not interested anymore to work for the TrainTrain start-up. This is why TrainTrain has been calling you to
code the new feature they want to add on their existing system.

## Existing business rules around seats reservations

1- For a train overall, no more than 70% of seats may be reserved in advance.

2 - You must put all the seats for one reservation in the same coach.

## Objective

Introduce a new business rule:


No individual coach should have no more than 70 percent of reserved seats. This
could make you and go over 70% for some coaches, just make sure to keep to 70% for the whole train.


[Source](https://github.com/42skillz/liveCoding-LegacyTrain-java)