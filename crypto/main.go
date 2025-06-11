package zkbancrypto

import (
	_ "embed"
	"encoding/json"

	client "github.com/akakou/zk-ban-system/client/signer"
	coresigner "github.com/akakou/zk-ban-system/core/signer"
)

//go:embed join_prover.json
var JoinProver []byte

//go:embed sign_prover.json
var SignProver []byte

//go:embed update_prover.json
var UpdateProver []byte

func RequestJoin(idToken, url string) ([]byte, error) {
	newSigner, err := client.RequestJoin(idToken, JoinProver, url)

	return newSigner, err
}

func Sign(message []byte, count int64, signer, gpk []byte) ([]byte, error) {
	signature, err := coresigner.Sign(message, count, signer, gpk, SignProver)
	if err != nil {
		return nil, err
	}

	res, err := json.Marshal(signature)
	if err != nil {
		return nil, err
	}

	return res, nil
}

func RequestUpdate(signer, rl, gpk []byte, url string) ([]byte, error) {
	newSigner, err := client.RequestUpdate(signer, rl, gpk, UpdateProver, url)

	return newSigner, err
}

func FetchGroupPublicKey(url string) ([]byte, error) {
	return client.FetchGroupPublicKey(url)
}

func FetchRevocationList(url string) ([]byte, error) {
	return client.FetchRevocationList(url)
}
