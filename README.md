# Neural Exception Handling Recommender

<p aligh="center"> This repository contains the code for <b>Neural Exception Handling Recommender</b>.</p>

# Source Code: https://github.com/trycatchresearch/try_catch_research/tree/master/DeepEx#Instruction_to_Run_DeepEx


## Contents
1. [Introduction](#Introduction)
2. [Dataset](#Dataset)
3. [Requirement](#Requirement)
4. [Instruction_to_Run_DeepEx](#Instruction_to_Run_DeepEx)

## Introduction

With practical code reuse, the code fragments from developer forums often migrate to applications. Owing to the incomplete nature of such fragments, they often lack the details on exception handling. The adaptation for exception handling to the codebase is not trivial as developers must learn and memorize what API methods could cause exceptions and what exceptions need to be handled. We propose DeepEx, a learning-based exception handling recommender, to accept a given Java code snippet and recommend if a try-catch block is needed, what statements need to be placed in a try-catch block, and what exception types need to be caught in the catch clause. We overcome the limitations of the state-of-the-art information retrieval approaches by our learning exception handling from complete code via Relational Graph Convolutional Network (R-GCN) and an explainable AI model. Via R-GCN, we consider the program dependencies in the surrounding context, which enables DeepEx to learn the identities of the APIs and the relations with the corresponding exception types needed to be handled. Our empirical evaluation shows that DeepEx relatively improves by 12.3% in F-score over the state-of-the-art approach in try-catch block necessity checking. It also achieves a high accuracy of 74% in suggesting the statements to be placed in a try-catch block. It can cover all the needed exception types in 63% of the cases and predict correctly all exception types in 33% of the cases. Our extrinsic evaluation also shows that DeepEx improves by 9.8% in F-score over the existing approach in detecting exception-related bugs.


## Dataset

You can download the data from: https://drive.google.com/file/d/1-9FnTWuOqrTxdxFKePA_evi5nEecbu3u/view?usp=sharing

## Requirement

Install ```Torch``` by following the Instruction from [PyTorch](https://pytorch.org/get-started/locally).

Install ```torch_sparse``` by following the Instruction from [pytorch_sparse](https://github.com/rusty1s/pytorch_sparse).

Install ```torch_geometric``` by following the Instruction from [torch_geometric](https://pytorch-geometric.readthedocs.io/en/latest/notes/installation.html).

Install ```scikit-learn``` by following the Instruction from [scikit-learn](https://scikit-learn.org/stable/getting_started.html).

Install ```networkx``` by following the Instruction from [networkx](https://networkx.org/documentation/stable/install.html).

Install ```optuna``` by following the Instruction from [optuna](https://optuna.org/#installation).

Install ```tqdm``` by following the Instruction from [tqdm](https://github.com/tqdm/tqdm).

## Instruction_to_Run_DeepEx

1. Download the dataset and save it in ```../DeepEx/data``` folder.

2. Download the DeepEx source code and run ```main.py``` to see the result for our experiment. 
