(defn get-lines
  "Load all lines from file file-path into memory"
  [file-path]
  (clojure.string/split-lines (slurp file-path)))

(count (get-lines "/Users/gordon/Dev/advent/day07.txt"))

(def node-regex #"^(?<name>[a-z]+) \((?<weight>\d+)\)( -> (?<children>.*))?")
(def file-path "/Users/gordon/Dev/advent/day07.txt")

(defn children
  [string]
  (if (nil? string)
    []
    (clojure.string/split string #", ")))

(defn parse-node
  [string]
  (let [match (re-matcher node-regex string)]
    (.matches match)
    {:name (.group match "name")
     :weight (.group match "weight")
     :children (children (.group match "children"))}))

(parse-node "bljxv (134) -> nbppbiy, npyxpyc")
(parse-node "occxa (60)")

(defn node-map
  [file-path]
  (->> (get-lines file-path)
       (map parse-node)
       (map (juxt :name identity))
       (flatten)
       (apply hash-map)))

(node-map file-path)

(defn nest-children
  [node-key node-map]
  (let [node (get node-map node-key)
        children (:children node)
        node-prime (assoc node :children (map #(get node-map %1) children))]
    (assoc node-map (:name node-prime) node-prime)))

(nest-children
  :a
  {:a {:name :a :children [:b :c]}
   :b {:name :b :children []}
   :c {:name :c :children []}})

(defn build-tree
  [node-map]
  (loop [nodes node-map
         node-list (keys node-map)]
    (if (empty? (rest node-list))
      nodes
      (recur
        (nest-children
          (first node-list)
          nodes)
        (rest node-list)))))

(build-tree (node-map file-path))
