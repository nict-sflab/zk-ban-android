package main

import (
	"fmt"
	"os"

	"github.com/akakou/zk-ban-system/core/core"
)

func main() {
	joinProver, joinVerify, err := core.JoinRequestCircuit()
	if err != nil {
		fmt.Printf("failed to generate join keys")
	}

	err = os.WriteFile("./join_prover.json", joinProver, 0644)
	if err != nil {
		fmt.Printf("failed to generate join keys")
	}

	err = os.WriteFile("./join_verifier.json", joinVerify, 0644)
	if err != nil {
		fmt.Printf("failed to generate join keys")
	}

	signProver, signVerifyKey, err := core.SignCircuit()
	if err != nil {
		fmt.Printf("failed to generate join keys")
	}

	err = os.WriteFile("./sign_prover.json", signProver, 0644)
	if err != nil {
		fmt.Printf("failed to generate join keys")
	}

	err = os.WriteFile("./sign_verifier.json", signVerifyKey, 0644)
	if err != nil {
		fmt.Printf("failed to generate join keys")
	}

	updateProver, updateVerify, err := core.UpdateCircuit([]int32{})
	if err != nil {
		fmt.Printf("failed to generate join keys")
	}

	err = os.WriteFile("./update_prover.json", updateProver, 0644)
	if err != nil {
		fmt.Printf("failed to generate join keys")
	}

	err = os.WriteFile("./update_verifier.json", updateVerify, 0644)
	if err != nil {
		fmt.Printf("failed to generate join keys")
	}

}
