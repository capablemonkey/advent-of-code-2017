(ns clj.core)

(defn diff
  "Compute the difference to be added to produce the redistributed vector"
  [number position buckets]
  (->> (concat
         (repeat (inc position) 0)
         (repeat number 1)
         (repeat 0))
       (partition buckets)
       (take (inc (Math/ceil (/ number buckets))))
       (apply map +)))

(diff 7 2 4)

(defn max-index [memory-banks]
 (->> (map vector (range) memory-banks)
      (sort-by (juxt second first) #(and (> (first %1) (first %2))
                                         (> (second %1) (second %2))))
      (ffirst)))

(max-index [2 0 7 3])
(max-index [2 0 7 7 3])
(max-index [2 0 7 7 7 3])

(defn redistribute
  [buckets]
  (map +
    (assoc buckets (max-index buckets) 0)
    (diff
      (apply max buckets)
      (max-index buckets)
      (count buckets))))

(redistribute [0 2 7 0])

(defn repeat-until-duplicate
  [buckets]
  (loop [seen (hash-set buckets)
         b buckets]
    (let [b-prime (vec (redistribute b))]
      (if (contains? seen b-prime)
        seen
        (recur
          (conj seen b-prime)
          b-prime)))))

(repeat-until-duplicate [0 2 7 0])
(count (repeat-until-duplicate [11 11 13 7 0 15 5 5 4 4 1 1 7 1 15 11]))
