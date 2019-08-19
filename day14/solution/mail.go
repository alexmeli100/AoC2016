package main

import (
	"crypto/md5"
	"errors"
	"fmt"
	"sort"
)

const (
	KeyStretch = 2017
	Input      = "zpqevtbw"
)

func match(value string, size int) (rune, error) {
	for i := 0; i < len(value)-(size-1); i++ {
		res := true
		for j := 0; j < size-1; j++ {
			res = res && (value[i+j] == value[i+j+1])
		}
		if res {
			return rune(value[i]), nil
		}
	}

	return 0, errors.New("can't find match")
}

func md5Times(input string, count int) string {
	hash := fmt.Sprintf("%x", md5.Sum([]byte(input)))
	for i := 0; i < count-1; i++ {
		hash = fmt.Sprintf("%x", md5.Sum([]byte(hash)))
	}

	return hash
}

func main() {
	hashes := make([]int, 0)
	threes := make(map[rune][]int)
	idx := int(0)

	for len(hashes) < 64 {
		hash := md5Times(fmt.Sprintf("%s%d", Input, idx), KeyStretch)
		val, err := match(hash[:], 5)

		if err == nil {
			for _, v := range threes[val] {
				if idx-v <= 1000 {
					hashes = append(hashes, v)
				}
			}
			threes[val] = []int{}
		}

		val, err = match(hash[:], 3)

		if err == nil {
			threes[val] = append(threes[val], idx)
		}

		idx++
	}

	sort.Ints(hashes)
	fmt.Printf("%d\n", hashes[63])
}
