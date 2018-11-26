---
layout: docs
title: "Functions"
section: "docs"
author: M.C. Oscar Vargas Torres
---

# Module 4: Example Contents

**Note**: [Scala Language Specification]
can be used as an authoritative reference in case of doubts.

[Scala Language Specification]: https://www.scala-lang.org/files/archive/spec/2.12/

## Function types

The following is an example of a simple function:

```scala
val triple = (n: Int) => n * 3
```

The *type* of this function literal is: `Int => Int`. We could have used
the *type annotation* to define triple:

```scala
val triple: Int => Int = n => n * 3
```

That can be read like:

> `triple` is a function that accepts an `Int` parameter and returns an
> `Int`

```scala
def fac(n: Int) = {
  var r = 1
  for (i <- 1 to n)
    r = r * i
  r
}
```

## `compose`

It has the following signature

```scala
def compose[A](g: (A) => T1): (A) => R
```

It models the mathematical function composition. For example, if
$f(x) = x + 1$ and $g(x) = 2x$,

$$
\begin{align}
(f \cdot g)(x) &= f(g(x)) = 2x + 1 \\
(g \cdot f)(x) &= g(f(x)) = 2(x + 1)
\end{align}
$$

## Currying

Let’s take a look at `Functions2` trait scaladoc. It is used for
functions that accept *two* arguments. For example:

```tut
val add1: (Int, Int) => Int = _ + _
```

A *curried* version of the above is:

```tut
val add2: Int => Int => Int = a => b => a + b
```

## Using Python

An example from <https://www.tensorflow.org/tutorials/>:

```python
import tensorflow as tf
mnist = tf.keras.datasets.mnist

(x_train, y_train),(x_test, y_test) = mnist.load_data()
x_train, x_test = x_train / 255.0, x_test / 255.0

model = tf.keras.models.Sequential([
  tf.keras.layers.Flatten(),
  tf.keras.layers.Dense(512, activation=tf.nn.relu),
  tf.keras.layers.Dropout(0.2),
  tf.keras.layers.Dense(10, activation=tf.nn.softmax)
])
model.compile(optimizer='adam',
              loss='sparse_categorical_crossentropy',
              metrics=['accuracy'])

model.fit(x_train, y_train, epochs=5)
model.evaluate(x_test, y_test)
```

## Using JavaScript

From <https://github.com/tensorflow/tfjs>:

```javascript
import * as tf from '@tensorflow/tfjs';

// Define a model for linear regression.
const model = tf.sequential();
model.add(tf.layers.dense({units: 1, inputShape: [1]}));

// Prepare the model for training: Specify the loss and the optimizer.
model.compile({loss: 'meanSquaredError', optimizer: 'sgd'});

// Generate some synthetic data for training.
const xs = tf.tensor2d([1, 2, 3, 4], [4, 1]);
const ys = tf.tensor2d([1, 3, 5, 7], [4, 1]);

// Train the model using the data.
model.fit(xs, ys).then(() => {
  // Use the model to do inference on a data point the model hasn't seen before:
  model.predict(tf.tensor2d([5], [1, 1])).print();
});
```

## Using Java

From [Train.java](https://raw.githubusercontent.com/tensorflow/models/master/samples/languages/java/training/src/main/java/Train.java):

```java
/* Copyright 2018 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.Tensors;

/**
 * Training a trivial linear model.
 */
public class Train {
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Require two arguments: The GraphDef file and checkpoint directory");
      System.exit(1);
    }

    final byte[] graphDef = Files.readAllBytes(Paths.get(args[0]));
    final String checkpointDir = args[1];
    final boolean checkpointExists = Files.exists(Paths.get(checkpointDir));

    try (Graph graph = new Graph();
        Session sess = new Session(graph);
        Tensor<String> checkpointPrefix =
            Tensors.create(Paths.get(checkpointDir, "ckpt").toString())) {
      graph.importGraphDef(graphDef);

      // Initialize or restore.
      // The names of the tensors in the graph are printed out by the program
      // that created the graph:
      // https://github.com/tensorflow/models/blob/master/samples/languages/java/training/model/create_graph.py
      if (checkpointExists) {
        sess.runner().feed("save/Const", checkpointPrefix).addTarget("save/restore_all").run();
      } else {
        sess.runner().addTarget("init").run();
      }
      System.out.print("Starting from       : ");
      printVariables(sess);

      // Train a bunch of times.
      // (Will be much more efficient if we sent batches instead of individual values).
      final Random r = new Random();
      final int NUM_EXAMPLES = 500;
      for (int i = 1; i <= 5; i++) {
        for (int n = 0; n < NUM_EXAMPLES; n++) {
          float in = r.nextFloat();
          try (Tensor<Float> input = Tensors.create(in);
              Tensor<Float> target = Tensors.create(3 * in + 2)) {
            // Again the tensor names are from the program that created the graph.
            // https://github.com/tensorflow/models/blob/master/samples/languages/java/training/model/create_graph.py
            sess.runner().feed("input", input).feed("target", target).addTarget("train").run();
          }
        }
        System.out.printf("After %5d examples: ", i*NUM_EXAMPLES);
        printVariables(sess);
      }

      // Checkpoint.
      // The feed and target name are from the program that created the graph.
      // https://github.com/tensorflow/models/blob/master/samples/languages/java/training/model/create_graph.py.
      sess.runner().feed("save/Const", checkpointPrefix).addTarget("save/control_dependency").run();

      // Example of "inference" in the same graph:
      try (Tensor<Float> input = Tensors.create(1.0f);
          Tensor<Float> output =
              sess.runner().feed("input", input).fetch("output").run().get(0).expect(Float.class)) {
        System.out.printf(
            "For input %f, produced %f (ideally would produce 3*%f + 2)\n",
            input.floatValue(), output.floatValue(), input.floatValue());
      }
    }
  }

  private static void printVariables(Session sess) {
    List<Tensor<?>> values = sess.runner().fetch("W/read").fetch("b/read").run();
    System.out.printf("W = %f\tb = %f\n", values.get(0).floatValue(), values.get(1).floatValue());
    for (Tensor<?> t : values) {
      t.close();
    }
  }
}
```