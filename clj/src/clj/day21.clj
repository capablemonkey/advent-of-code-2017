(def start-input ".#./..#/###")
(def patterns-input [".#./..#/### => #..#/..../..../#..#"
                     "../.# => ##./#../...")

(defn get-lines
  "Load all lines from file file-path into memory"
  [file-path]
  (clojure.string/split-lines (slurp file-path)))

(count (get-lines "/Users/gordon/Dev/advent/day21.txt"))

(defn split-by
  [delimiter string]
  (clojure.string/split string delimiter))

(split-by #"/" start-input)

(defn char-list
  [string-form]
  (map #(apply list %1) string-form))

(char-list (split-by #"/" start-input))

(defn parse-pattern
  [string]
  (map
    #(split-by #"/" %1)
    (split-by #" => " string)))

(parse-pattern ".#./..#/### => #..#/..../..../#..#")

(defn build-pattern-map
  "Build up a map of patterns: match -> output"
  [pattern-lines]
  (->> pattern-lines
    (map parse-pattern)
    (apply concat)
    (map char-list)
    (apply hash-map)))

(def patterns (build-pattern-map (get-lines "/Users/gordon/Dev/advent/day21.txt")))
(def starting-pattern (char-list (split-by #"/" start-input)))

(defn row-group-to-blocks
  [n row-group]
  (->> row-group
    (map #(partition n %1))
    (apply map list)))

(row-group-to-blocks 2 ["#..#" "...."])

(defn blocks
  [n grid]
  (->> grid
    (partition n)
    (map #(row-group-to-blocks n %1))))

(blocks 2 ["#..#" "...." "...." "#..#"])

(defn blocks-to-grid
  [blocks]
  (->> blocks
    (map #(apply map concat %1))
    (apply concat)
    (map #(clojure.string/join "" %1))))

(blocks-to-grid (blocks 2 ["#..#" "...." "...." "#..#"]))

(defn print-grid
  [grid]
  (doseq [line grid]
    (println line))
  grid)

(print-grid
  (blocks-to-grid (blocks 2 ["#..#" "...." "...." "#..#"])))

(defn print-blocks
  [blocks]
  (doseq [block-row blocks]
    (doseq [row (apply map list block-row)]
      (println row)))
  blocks)

(print-blocks
  (blocks 2 ["#..#" "...." "...." "#..#"]))

(defn rotate-block
  "Rotating a block is the same as turning its columns into rows
  starting from the bottom"
  [block]
  (apply map list (reverse block)))

(rotate-block (ffirst (blocks 2 ["#..#" "...." "...." "#..#"])))

(defn horizontal-flip-block
  "Horizontal flip is the same as reversing each row"
  [block]
  (map reverse block))

(horizontal-flip-block (ffirst (blocks 2 ["#..#" "...." "...." "#..#"])))

(defn vertical-flip-block
  [block]
  (reverse block))

(vertical-flip-block (ffirst (blocks 2 ["#..#" "...." "...." "#..#"])))

(defn all-orientations
  [block]
  (for [flip [horizontal-flip-block vertical-flip-block identity]
        rotation [rotate-block
                   (comp rotate-block)
                   (comp rotate-block rotate-block)
                   (comp rotate-block rotate-block rotate-block)]]
    (flip (rotation block))))

(all-orientations (ffirst (blocks 2 ["#..#" "...." "...." "#..#"])))

(defn find-pattern
  [patterns block]
  (some identity
    (map
      patterns
      (all-orientations block))))

(find-pattern patterns (ffirst (blocks 2 ["#..#" "...." "...." "#..#"])))

(defn expand
  [patterns blocks]
  (for [block-row blocks]
    (map #(find-pattern patterns %1) block-row)))

(print-blocks
  (expand patterns (blocks 2 ["#..#" "...." "...." "#..#"])))

(print-blocks
  (expand patterns (blocks 3 starting-pattern)))

(->> starting-pattern
  (print-grid)
  (find-pattern patterns)
  (blocks 2)
  (print-blocks)
  (expand patterns)
  (print-blocks)
  (blocks-to-grid)
  (blocks 2)
  (expand patterns)
  (print-blocks)
  (expand patterns)
  (print-blocks)
  (blocks-to-grid)
  (blocks 2)
  (expand patterns)
  (print-blocks)
  (flatten)
  (frequencies))

(defn row-length
  [block-row]
  (*
    (count block-row)
    (count (ffirst block-row))))

(defn expand-loop
  [n blockz]
  (loop [n n
         blockz blockz]
    ; (print-blocks blockz)
    (if (= n 0)
      (frequencies (flatten blockz))
      (recur
        (dec n)
        (->> blockz
          (blocks-to-grid)
          (blocks (if (= 0 (mod (row-length blockz) 2))
                      2
                      3))
          (expand patterns))))))

(expand-loop 4 (blocks 2 (find-pattern patterns starting-pattern)))
(expand-loop 17 (blocks 2 (find-pattern patterns starting-pattern)))
