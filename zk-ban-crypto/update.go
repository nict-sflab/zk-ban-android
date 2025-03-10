package zkbancrypto

import (
	"bytes"
	"encoding/json"
	"net/http"

	highlevel "github.com/akakou/zk-ban/highlevel"
	"github.com/akakou/zk-ban/witness"
)

type UpdateRequest struct {
	Before        int64
	After         int64
	UserPublicKey []byte
	Proof         []byte
}

func RequestUpdate(period int64, signer, rl, gpk, prover []byte, url string) []byte {
	aaa, err := requestUpdate(period, signer, rl, gpk, prover, url)
	result := Result{
		Result: aaa,
		Error:  err,
	}

	return result.Output()
}

func requestUpdate(period int64, signer, rl, gpk, prover []byte, url string) (any, error) {
	proverObj := highlevel.HighLevelSnarkProver{}
	err := json.Unmarshal(prover, &proverObj)

	if err != nil {
		return nil, err
	}

	rlObj := witness.RevocationList{}
	err = json.Unmarshal(rl, &rlObj)

	if err != nil {
		return nil, err
	}

	signerObj := highlevel.HighLevelSigner{}
	err = json.Unmarshal(signer, &signerObj)

	if err != nil {
		return nil, err
	}

	updatedSigner, proof, err := highlevel.UpdateRequest(period, &signerObj, rlObj, gpk, &proverObj)
	if err != nil {
		return nil, err
	}

	req := UpdateRequest{
		After:         updatedSigner.Period,
		Before:        signerObj.Period,
		UserPublicKey: updatedSigner.UserPublicKey,
		Proof:         proof,
	}

	reqBytes, err := json.Marshal(req)
	if err != nil {
		return nil, err
	}

	res, err := http.Post(url, "application/json", bytes.NewBuffer(reqBytes))
	if err != nil {
		return nil, err
	}

	defer res.Body.Close()

	buf := new(bytes.Buffer)
	_, err = buf.ReadFrom(res.Body)
	if err != nil {
		return nil, err
	}

	updatedSigner.Credential = buf.Bytes()

	return &updatedSigner, nil
}
