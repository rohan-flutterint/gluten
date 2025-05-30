/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.gluten.execution

import org.apache.gluten.backendsapi.arrow.ArrowBatchTypes.ArrowNativeBatchType
import org.apache.gluten.backendsapi.velox.VeloxBatchType
import org.apache.gluten.columnarbatch.VeloxColumnarBatches

import org.apache.spark.sql.execution.SparkPlan
import org.apache.spark.sql.vectorized.ColumnarBatch

case class ArrowColumnarToVeloxColumnarExec(override val child: SparkPlan)
  extends ColumnarToColumnarExec(ArrowNativeBatchType, VeloxBatchType) {
  override protected def mapIterator(in: Iterator[ColumnarBatch]): Iterator[ColumnarBatch] = {
    in.map {
      b =>
        val out = VeloxColumnarBatches.toVeloxBatch(b)
        out
    }
  }
  override protected def withNewChildInternal(newChild: SparkPlan): SparkPlan =
    ArrowColumnarToVeloxColumnarExec(child = newChild)
}
