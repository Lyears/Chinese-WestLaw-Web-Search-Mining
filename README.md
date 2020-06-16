# Chinese-WestLaw-Web-Search-Mining
A simple Chinese WestLaw Web searching system, as the final project for WSM2020, SJTU.

## Basic Information
This project is a simple information retrieval system for Chinese WestLaw dataset.
Specifically, users can search for certain law cases with keywords in their items, 
such as case code, court name, business entity. 
The retrieval system will return all cases that satisfy the conditions specified by users.

The project provides the following functionalities.
- a boolean retrieval interface for law cases
- a one-shot retrieval interface for law cases
- a content-based retrieval interface for instruments of law cases
- an option of sorting all law cases retrieved by some specific items
- a pleasant frontend

## Prerequisites

### Requirements
Our system has the following requirements.
- JDK 11
- Gradle, version 6.4.1
- git

### Other preparations

You can clone our information retrieval project from github.
```
git clone git@github.com:zhengjilai/Chinese-WestLaw-Web-Search-Mining.git
cd Chinese-WestLaw-Web-Search-Mining && export PROJECT_DIR=$(pwd)
```

Before building the project, please also set up the environment variable `WSM_ROOT_DIR` in your shell.
This directory is the home path for all other resources, such as dataset and all the indexes constructed.

```
export WSM_ROOT_DIR=/your/path/to/all/resources
```

For several reasons, we recommend you to separate `WSM_ROOT_DIR` from `PROJECT_DIR`. 

### Chinese WestLaw Dataset
Chinese WestLaw dataset is provided by [Kenny Zhu](http://cs.sjtu.edu.cn/~kzhu/), 
with court records of legal cases in China in Chinese, collected from three different sources.

You can download raw data, unzip all three parts as follows.

```
cd $WSM_ROOT_DIR
wget https://adapt.seiee.sjtu.edu.cn/wsm2020/wsm_proj1_data/data1.zip
wget https://adapt.seiee.sjtu.edu.cn/wsm2020/wsm_proj1_data/data2.zip
wget https://adapt.seiee.sjtu.edu.cn/wsm2020/wsm_proj1_data/instruments.zip
unzip data1.zip && unzip data2.zip && unzip instruments.zip && rm *.zip
```

As long as you have set up the environment variable `WSM_ROOT_DIR` and downloaded all raw data, 
our project can work without any extra configuration.

## Usage

### Index Construction

As an typical information retrieval system, building index is necessary. 
In this project, we build Boolean indexes for all items and vector-based TF-IDF index for instruments.
You can construct all indexes in `WSM_ROOT_DIR` with the following commands. 

```
cd $PROJECT_DIR
./gradlew init_tfidf && ./gradlew init_boolean_index
```

Please set `WSM_ROOT_DIR` before executing the above gradle tasks. 
On a computer with 32G RAM and Intel(R) Core(TM) i7-8700K CPU @ 3.70GHz, 
the index construction process takes about 15 minutes.

### Service Deployment

We provide two alternatives for deploying the IR service, local deployment or docker deployment.
For either of the above choices, you should first build the service, written by SpringBoot.

```
cd $PROJECT_DIR
./gradlew build
```

For local deployment, you can directly execute the following command, 
and the service will start at your `8080` port.

For docker deployment, you can first build the docker image with `docker build`,
and then start up the service by creating a docker container.

```
cd $PROJECT_DIR
docker build -t wsm2020:0.0.1 .
docker run -v $WSM_ROOT_DIR:/srv/wsm/root -p 8080:8080 wsm2020:0.0.1 
```

## Contributors
- [Jilai Zheng](https://github.com/zhengjilai)
- [Zhiming Fan](https://github.com/Lyears)
