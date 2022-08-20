# Baltimore Street Searcher

Author: Chase Feng  
Date: May 2022

## Description

This project is an application which traverses the streets of Baltimore to find the shortest route from one point to
another. Using satellite map data, the application takes the GPS coordinates of a starting and ending intersection, and
returns the directions and total distance (in meters) of the sequence of roads which connects them with the minimum
distance travelled. 


## Instructions

Before running the application, edit the `getConfig()` function in `Config.java` in the hw8 package:

    public static Config getConfig() {
        /* Sample valid endpoints */
        return new Config("baltimore.streets.txt", "-76.6107,39.2866", "-76.6175,39.3296");
    }

The first input tells the application which "map" to use. Four different maps are available, each encoded as text files:
* **baltimore.streets.txt** (Default): Map of streets between intersections across the City of Baltimore
* **campus.paths.txt**: Map of paths between landmarks across the campus of Johns Hopkins University
* **baseball.txt**: Map of paths between bases on a baseball field
* **broken.txt**: Map of paths between nodes in a hypothetical [unconnected graph](https://en.wikipedia.org/wiki/Connectivity_(graph_theory)#Connected_vertices_and_graphs)


The second and third inputs specify the GPS coordinates (longitude and latitude) of the desired starting and ending points.
We have assumed start and end at intersections between different road segments and not at arbitrary GPS coordinates.
Therefore, please input GPS coordinates appearing in the file you are using.

T
follow the instructions annotated with a TODO, before running the driver program.)

Queries support the following operations, which can be combined in postfix notation
* _[keyword]_: Fetches all URLs containing the specified keyword
* _&&_: Fetches all URLs that match both the first query and the second query
* _||_: Fetches all URLs that match either the first query or the second query
* _?_: Prints the URLs corresponding to the most recent query result (based on a single keyword input or an operation result)
* _!_: Quits the program

A program run might resemble the following:

    === JHUgle Search Engine Running! ===
    >>> Add your query below >>>
    > ?
    > baz
    > red
    > ?
    https://en.wikipedia.org/wiki/Cat
    http://www.foobar.com/baz
    http://www.cars.com/
    > &&
    > ?
    http://www.foobar.com/baz
    > !
    
    Process finished with exit code 0

## Data

Four different "maps" are available, each encoded as a text file.
* baltimore.streets.txt:
* _&&_: Fetches all URLs that match both the first query and the second query
* _||_: Fetches all URLs that match either the first query or the second query
* _?_: Prints the URLs corresponding to the most recent query result (based on a single keyword input or an operation result)
* _!_: Quits the program

They are encoded as text files in `src/main/resources`, with  

Users can control what data file is fed to JHUgle through Config.DATA_FILENAME.

Several data files have been included in `src/main/resources`, ranging from short to very large.

## Implementation

Different varieties of the Hash Table data structure make up the core logic of our search engine. With operations of **O(1)**,
Hash Tables enable efficient lookups, which makes them ideal for searching over large amounts of data.

All Hash Tables in JHUgle have been implemented with only arrays, and without using Java's built-in maps. There are
implementations of a **Chaining Hash Map** and **Open Addressing Hash Map**, which provide different strategies for
Collision Resolution.

## Testing and Profiling

Tests have been written in JUnit, and can be found can be found `src/test/java/hw7`. In particular, they verify the
accuracy of the map implementations and their core implementations. There is also a benchmark test `JmhRuntimeTest.java`,
which has been designed to build JHUgle with different data files and profile its time and memory performance.