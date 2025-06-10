package zkbancrypto

import (
	client "github.com/akakou/zk-ban-system/client"
)

func Register(idToken, prover []byte, url string) []byte {
	signer, err := client.RequestJoin(idToken, prover, url)

	result := Result{
		Result: signer,
		Error:  err,
	}

	return result.Output()
}
