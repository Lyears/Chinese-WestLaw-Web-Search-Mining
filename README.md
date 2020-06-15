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

Before building up the project, please also set up the environment variable `WSM_ROOT_DIR` in your shell.
This directory is the home path for all other resources, such as dataset, and all the indexes constructed.

```
export WSM_ROOT_DIR=/your/path/to/all/resources
```

For several reasons, we recommend you to separate `WSM_ROOT_DIR` from `PROJECT_DIR`. 

## Dataset
The WestLaw dataset is provided by [Kenny Zhu](http://cs.sjtu.edu.cn/~kzhu/), 
with court records of legal cases in China in Chinese, collected from three different sources.

You can download raw data, unzip all three parts as follows.

```
cd $WSM_ROOT_DIR
wget https://adapt.seiee.sjtu.edu.cn/wsm2020/wsm_proj1_data/data1.zip
wget https://adapt.seiee.sjtu.edu.cn/wsm2020/wsm_proj1_data/data2.zip
wget https://adapt.seiee.sjtu.edu.cn/wsm2020/wsm_proj1_data/instruments.zip
unzip data1.zip && unzip data2.zip && unzip instruments.zip && rm *.zip
```

As long as you have set up the environment variable `WSM_ROOT_DIR`, 
our project can work without any extra configuration.

## Contributors
- [Jilai Zheng](https://github.com/zhengjilai)
- [Zhiming Fan](https://github.com/Lyears)