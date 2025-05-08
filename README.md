![image](https://github.com/user-attachments/assets/382058a2-492a-4b62-a36e-ba54849f179f)

## Background

Froth flotation is the most widely employed mineral separation technique, exploiting the differences in the physicochemical properties of mineral surfaces. In this process, hydrophobic mineral particles selectively attach to gas bubbles, rising to form a froth layer, while hydrophilic particles remain in the slurry. The characteristics of the froth layer — including bubble size, stability, and texture — are critical indicators of flotation performance and process conditions.

Traditionally, operators monitor flotation by visually inspecting the froth, adjusting operating conditions based on experience. However, manual observation is highly subjective, inconsistent, and prone to human error, leading to suboptimal flotation efficiency and control.

## About this Project

This project addresses the limitations of traditional monitoring by introducing **machine vision** and **graph-based deep learning** to automatically analyze froth images. Using **digital image processing** techniques, we quantitatively extract features from froth images, construct a patch-level graph based on feature similarity, and apply a **Graph Neural Network (GNN)** to model and predict flotation performance.
## 1. Feature Extraction

The froth surface image is first pre-processed to enhance feature extraction:

- **Noise Reduction**: A Gaussian filter is applied to the image to suppress high-frequency noise while preserving edge structures.
    
- **Edge Detection**: Canny Edge Detection is then used to delineate bubble boundaries, capturing the morphology of the froth.
    
- **Edge Density**: The total count of edge pixels relative to the image area provides a quantitative measure of froth coalescence and bubble size distribution.
    

Beyond structural features, **color** and **texture** information are also extracted:

- **Average RGB Color**: Mean values across the Red, Green, and Blue channels capture colorimetric variations associated with chemical properties and frothier concentrations.
    
- **Texture Features**: Using **Gray-Level Co-occurrence Matrix (GLCM)**, we compute:
    
  - **Contrast**: Quantifies local intensity variations, indicating heterogeneity in bubble sizes.
  - **Energy**: Measures textural uniformity, related to bubble arrangement and stability.
  - **Entropy**: Reflects the complexity or randomness of the texture.
  - **Homogeneity**: Measures the closeness of the distribution of elements to the GLCM diagonal, indicating the smoothness and uniformity of the bubble structures.
        

Each image is divided into smaller non-overlapping **patches**, and an 8-dimensional feature vector is extracted for each patch, capturing its local visual properties.

## 2. Graph Construction

After patch-level feature extraction, we represent the image as a **graph structure**:

- **Nodes**: Each patch is a node associated with its feature vector.
    
- **Edges**: A **K-Nearest Neighbors (KNN)** algorithm (with K=15K = 15K=15) connects each node to its 15 most similar patches based on **cosine similarity** between their feature vectors.
    

The resulting **undirected graph** encodes the spatial and visual relationships within the froth image. It enables localized patterns such as clustering of similar bubbles or textural transitions to be learned effectively

## 3. Graph Neural Network (GNN) Modeling

The graph is then fed into a **Graph Neural Network (GNN)** to perform classification or regression tasks relevant to flotation performance monitoring.

### GNN Layer Mechanics

The GNN architecture operates through **message passing**:

1. **Message Aggregation**: Each node collects information from its neighbors by aggregating neighboring features. Aggregation is typically a weighted sum where weights can be uniform or learned.
    
2. **Feature Update**: The aggregated neighbor features are combined with the node’s own features through a learnable transformation, followed by a **non-linear activation** (ReLU):

$$
h_v^{(k+1)} = \text{ReLU}\left( W \cdot \text{AGGREGATE}\left( \{ h_u^{(k)} : u \in N(v) \} \cup \{ h_v^{(k)} \} \right) \right)
$$
	$$( h_v^{(k)} ) \ \ is  \ \ the \ \ node’s  \ \ feature  \ \ vector  \ \ at  \ \ layer \ ( k ), \ \
	- \ ( N(v)) \ \ is \ \ the  \ \ set  \ \ of  \ \ neighbors \ \ of  \ \ node \ ( v ),
	- \ \ ( W)  \ \ is  \ \ a  \ \ learnable \ \ weight  \ \ matrix.$$


This iterative message-passing updates the node embeddings, allowing each node to incorporate multi-hop neighborhood information.

### GNN Training and Readout

- **Pooling**: After several GNN layers, a **global pooling** operation (mean or sum pooling) aggregates all node embeddings into a single vector representing the entire image.
    
- **Scoring**: The pooled feature vector is passed through a linear transformation followed by a **sigmoid activation** to predict a continuous or binary score associated with the image (e.g., froth quality).
    
- **Loss Function**:
    
    - For binary outcomes (e.g., good vs poor flotation), **Binary Cross-Entropy Loss** is used.
        
    - Loss is computed between the predicted score and the ground-truth label, driving the optimization.
        
- **Backpropagation and Optimization**:
    
    - Gradients of the loss with respect to model parameters are computed via backpropagation.
        
    - Parameters are updated using **gradient descent**, progressively improving the GNN’s predictive capability.


### Testing Phase 

During the testing phase, the model evaluates new, unseen images without updating its internal parameters. The following steps are performed:

- The model receives an unseen image and constructs a K-Nearest Neighbors (KNN) graph based on the image’s feature vectors.
    
- Through message passing across the graph neural network layers, node feature representations are updated.
    
- After message passing, the model uses the learned `linearWeights` from the training phase to compute a prediction score for each node.
    
- The final prediction for the image is obtained by averaging the scores across all nodes.
    
- A classification decision is made based on the average score:
    
    - If the average score is **greater than or equal to 0.58**, the image is classified as a **"Good Image"**.
        
    - If the average score is **less than 0.58**, the image is classified as a **"Bad Image"**.
        
- Importantly, **no further weight updates occur during testing**; the model uses the parameters optimized during training to make predictions.


### Model Performance

| Prediction Type       | Good Images (10 total) | Bad Images (10 total) |
|:-----------------------|:-----------------------|:----------------------|
| Correctly Predicted     | 6                      | 8                     |
| Incorrectly Predicted   | 4                      | 2                     |


- **Accuracy for good images:**

  Accuracy = 6 / 10 = 60%

- **Accuracy for bad images:**

  Accuracy = 8 / 10 = 80%

- **Overall samples:**

  20 images total.

- **Overall correct predictions:**

  6 + 8 = 14

- **Overall accuracy:**

  Accuracy = 14 / 20 = 70%


### Confusion Matrix

|                        | Predicted Good | Predicted Bad |
|:-----------------------|:--------------:|:-------------:|
| **Actual Good**         | 6 (True Positive) | 4 (False Negative) |
| **Actual Bad**          | 2 (False Positive) | 8 (True Negative) |

- **True Positive (TP):** 6  
- **True Negative (TN):** 8  
- **False Positive (FP):** 2  
- **False Negative (FN):** 4
